package com.tronography.rxmemory.ui.game.viewmodel

import DEBUG
import ERROR
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.model.cards.Card
import com.tronography.rxmemory.data.repository.Repository
import com.tronography.rxmemory.data.state.GameState
import com.tronography.rxmemory.data.state.GameState.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class GameViewModel
@Inject constructor(
        private val repository: Repository
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val flippedCards = HashMap<String, Card>()
    private val matchedCards = HashMap<String, Card>()

    private var attemptCount = ZERO
    private var liveAttemptCount = MutableLiveData<Int>()

    private var firstCardSelected: Card? = null

    val gameStateDataMerger: MediatorLiveData<GameState> = MediatorLiveData()

    private var gameState = MutableLiveData<GameState>()

    init {
        DEBUG("Initializing GameViewModel")
        startGame()
    }

    fun observeDeck(): LiveData<List<Card>> {
        return repository.getCards()
    }

    fun refreshCards() {
        DEBUG("refreshCards called")
        gameStateDataMerger.addSource(repository.getLiveGameState(), { state -> gameStateDataMerger.value = state })
        gameStateDataMerger.addSource(gameState, { state -> gameStateDataMerger.value = state })
        repository.createNewPokemonDeck()
    }

    fun getAttemptCount(): LiveData<Int> {
        return liveAttemptCount
    }

    fun getGameState(): LiveData<GameState> {
        return gameStateDataMerger
    }

    fun startGame() {
        broadcastGameState(LOADING)
        clearMediatorSources()
        flippedCards.clear()
        matchedCards.clear()
        attemptCount = ZERO
        refreshCards()
    }

    private fun clearMediatorSources() {
        gameStateDataMerger.removeSource(repository.getLiveGameState())
        gameStateDataMerger.removeSource(gameState)
    }

    fun onCardClicked(card: Card) {
        if (isNotSelected(card)) {
            selectCard(card)
        }
    }

    private fun selectCard(card: Card) {
        when (flippedCards.isEmpty()) {
            true -> {
                disposables.add(flipFirstCard(card))
            }
            false -> {
                DEBUG("second item_card selected = ${card.description}")
                incrementAttemptCount()
                broadcastGameState(RESETTING_CARDS)
                disposables.add(
                        Observable.just(card.selectCard())
                                .subscribeOn(Schedulers.io())
                                .compose(addToFlippedCardMap())
                                .compose(isValidMatch())
                                .compose(markAsMatched())
                                .switchMap { delay(ONE_SECOND) }
                                .compose(getUnmatchedCards())
                                .compose(resetUnmatchedCardsWithDelay())
                                .compose(clearFlippedCardMap())
                                .compose(clearSelectedCard())
                                .compose(isGameOver())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(enableCardClicks())
                                .doOnError { e -> ERROR("${e.message}") }
                                .subscribe())
            }
        }
    }

    private fun isValidMatch(): ObservableTransformer<Card, Boolean> {
        return ObservableTransformer {
            it.concatMap {
                Observable.fromCallable { isValidMatch(it) }
            }
        }
    }

    private fun markAsMatched(): ObservableTransformer<Boolean, HashMap<String, Card>> {
        return ObservableTransformer {
            it.concatMap { isMatch ->
                if (isMatch) {
                    return@concatMap Observable.fromCallable {
                        flippedCards.values.forEach {
                            val matchedCard = it.matchCard()
                            updateFlippedCards(matchedCard)
                            updateMatchedCards(matchedCard)
                        }
                        return@fromCallable flippedCards
                    }
                } else {
                    return@concatMap Observable.just(flippedCards)
                }
            }
        }
    }

    private fun flipFirstCard(card: Card): DisposableObserver<Card> {
        return Observable.just(card)
                .doOnSubscribe { DEBUG("Flipping first item_card : ${card.description}") }
                .subscribeOn(Schedulers.io())
                .compose(selectFirstCard())
                .compose(addToFlippedCardMap())
                .subscribeWith(object : DisposableObserver<Card>() {
                    override fun onComplete() {
                        //do nothing
                    }

                    override fun onNext(card: Card) {
                        DEBUG("Flipped $card")
                    }

                    override fun onError(e: Throwable) {
                        ERROR("Unable to flip first item_card. ${e.message}")
                    }
                })
    }

    private fun delay(delay: Long) = Observable.timer(delay, TimeUnit.MILLISECONDS)

    private fun selectFirstCard(): ObservableTransformer<Card?, Card?> {
        return ObservableTransformer {
            it.concatMap { card: Card? ->
                firstCardSelected = card?.selectCard()
                return@concatMap Observable.just(firstCardSelected)
            }
        }
    }

    private fun addToFlippedCardMap(): ObservableTransformer<Card?, Card> {
        return ObservableTransformer {
            it.concatMap { selectedCard: Card ->
                Observable.fromCallable {
                    flippedCards.put(selectedCard.cardId.toString(), selectedCard)
                    repository.insertCard(selectedCard)
                    return@fromCallable selectedCard
                }
            }
        }
    }

    private fun updateFlippedCards(card: Card) {
        flippedCards.put(card.cardId.toString(), card)
        repository.insertCard(card)
    }

    private fun updateMatchedCards(matchedCard: Card) {
        matchedCards.put(matchedCard.cardId.toString(), matchedCard)
        repository.updatePokemonAsCaught(matchedCard.pokemonId, true)
        repository.insertCard(matchedCard)
    }

    private fun getUnmatchedCards(): ObservableTransformer<Long, List<Card>> {
        return ObservableTransformer {
            it.flatMap {
                Observable.fromCallable {
                    flippedCards.values.filter { !it.isMatched }
                }
            }
        }
    }

    private fun resetUnmatchedCardsWithDelay(): ObservableTransformer<List<Card>, Unit> {
        return ObservableTransformer {
            it.concatMap { cards ->
                Observable.fromCallable {
                    cards.forEach { repository.insertCard(it.resetCard()) }
                }
            }
        }
    }

    private fun clearFlippedCardMap(): ObservableTransformer<Unit, Unit> {
        return ObservableTransformer {
            it.concatMap { Observable.fromCallable { flippedCards.clear() } }
        }
    }

    private fun clearSelectedCard(): ObservableTransformer<Unit, Unit> {
        return ObservableTransformer {
            it.concatMap {
                Observable.fromCallable { firstCardSelected = null }
            }
        }
    }

    private fun isGameOver(): ObservableTransformer<Unit, Boolean> {
        return ObservableTransformer {
            it.concatMap {
                Observable.fromCallable {
                    allCardsMatched()
                }
            }
        }
    }

    private fun allCardsMatched() = matchedCards.size == DECK_SIZE

    private fun enableCardClicks(): ObservableTransformer<Boolean, Unit> {
        return ObservableTransformer {
            it.concatMap { isGameOver ->
                Observable.fromCallable {
                    ERROR("isGameOver = $isGameOver")
                    if (isGameOver) {
                        repository.deleteCardTable()
                        broadcastGameState(GAME_OVER)
                    } else {
                        broadcastGameState(IN_PROGRESS)
                    }
                }
            }
        }
    }

    private fun isNotSelected(card: Card): Boolean {
        DEBUG("isNotSelected(${card.description})${!card.isSelected && !card.isMatched}")
        return !card.isSelected && !card.isMatched
    }

    private fun isValidMatch(card: Card): Boolean {
        return firstCardSelected?.cardId != card.cardId && firstCardSelected?.pokemonId == card.pokemonId
    }

    private fun incrementAttemptCount(): Int {
        attemptCount++
        liveAttemptCount.value = attemptCount
        return attemptCount
    }

    private fun broadcastGameState(state: GameState) {
        gameState.value = state
    }

    companion object {
        private const val ONE_SECOND: Long = 1000
        private const val DECK_SIZE = 16
        private const val ZERO = 0
    }

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
        repository.deleteCardTable()
        DEBUG("GameViewModel : CLEARED")
    }

    private fun clearDisposables() {
        DEBUG("Clearing Disposables")
        disposables.dispose()
        DEBUG("Disposables cleared : ${disposables.isDisposed}")
    }
}