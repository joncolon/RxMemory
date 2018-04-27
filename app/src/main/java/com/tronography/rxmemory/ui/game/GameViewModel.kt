package com.tronography.rxmemory.ui.game

import DEBUG
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.model.Card
import com.tronography.rxmemory.data.repository.Repository
import com.tronography.rxmemory.data.state.GameState
import com.tronography.rxmemory.data.state.GameState.*
import com.tronography.rxmemory.utilities.GlideUtils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import runOnMainThread
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GameViewModel
@Inject constructor(
        private val repository: Repository,
        private val glideUtils: GlideUtils
) : ViewModel() {


    private var gameState = MutableLiveData<GameState>()
    private var flippedCardCount = MutableLiveData<Int>()

    private val matchedCards = HashMap<String, Card>()
    private val flippedCards = HashMap<String, Card>()

    private var selectedCard: Card? = null

    private var attemptCount: Int

    private val disposables = CompositeDisposable()

    init {
        broadcastGameState(NOT_IN_PROGRESS)
        DEBUG("Initializing GameViewModel")
        attemptCount = ZERO
        refreshCards()
    }

    fun observeDeck(): LiveData<List<Card>> {
        return repository.getCards()
    }

    fun observeFlipCount(): LiveData<Int> {
        return flippedCardCount
    }

    fun refreshCards() {
        broadcastGameState(IN_PROGRESS)
        repository.createNewDeck()
    }

    fun getAttemptCount(): Int {
        return attemptCount
    }

    fun getGameState(): LiveData<GameState> {
        return gameState
    }

    fun restartGame() {
        broadcastGameState(RESTARTING)
        flippedCards.clear()
        matchedCards.clear()
        attemptCount = 0
        isMaxCardsFlipped()
        broadcastGameState(LOADING)
        refreshCards()
    }

    fun onCardClicked(card: Card) {
        DEBUG("isNotSelected(${card.description}) = ${isNotSelected(card)}")
        if (isNotSelected(card)) {
            selectCard(card)
            isMaxCardsFlipped()
        }
    }

    private fun selectCard(card: Card) {
        when (flippedCards.isEmpty()) {
            true -> {
                setSelectedCard(card)
                selectedCard?.let { updateFlippedCards(it) }
            }
            false -> {
                incrementAttemptCount()
                isMaxCardsFlipped()
                disposables.add(
                        Completable.fromAction { validateCardMatch(card) }
                                .subscribeOn(Schedulers.io())
                                .doOnComplete {
                                    resetUnmatchedFlippedCards(flippedCards)
                                }
                                .doAfterTerminate { runOnMainThread { isMaxCardsFlipped() } }
                                .subscribe())
            }
        }
    }

    private fun setSelectedCard(card: Card) {
        selectedCard = card.selectCard()
    }

    private fun validateCardMatch(card: Card) {
        if (isValidMatch(card)) {
            markAsMatched(card)
        } else {
            updateFlippedCards(card)
        }
    }

    private fun markAsMatched(card: Card) {
        updateFlippedCards(card)
        flippedCards.values.forEach {
            val matchedCard = it.matchCard()
            updateFlippedCards(matchedCard)
            updateMatchedCards(matchedCard)
        }
    }

    private fun updateFlippedCards(card: Card) {
        DEBUG("updating flippedCards")
        val flippedCard = card.copy(isFlipped = true, isSelected = true)
        flippedCards.put(flippedCard.cardId, flippedCard)
        DEBUG("flippedCards = ${flippedCards.values}")
        DEBUG("flippedCards.size = ${flippedCards.size}")

        repository.insertCard(flippedCard)
    }

    private fun preloadCardImages(cards: List<Card>) {
        cards.forEach { card -> glideUtils.preloadImages(card.photoUrl) }
    }

    private fun isGameLoading() = gameState.value == LOADING

    private fun resetUnmatchedFlippedCards(flippedCards: HashMap<String, Card>) {
        DEBUG("Resetting unmatched cards in flippedCards : ${flippedCards.keys}")
        delayedAction({ flipUnmatchedCards(flippedCards) }, ONE_SECOND)
    }

    private fun isGameOver() {
        DEBUG("matchedCards.size = ${matchedCards.size}")
        if (matchedCards.size == DECK_SIZE) {
            broadcastGameState(GAME_OVER)
        }
    }

    private fun updateMatchedCards(matchedCard: Card) {
        matchedCards.put(matchedCard.cardId, matchedCard)

        DEBUG("$matchedCard added to matchedCards")
        DEBUG("matchedCards.size = ${matchedCards.size}")

        repository.insertCard(matchedCard)
    }

    private fun flipUnmatchedCards(cards: HashMap<String, Card>) {
        cards.values.filter { !it.isMatched }
                .forEach {
                    val resetCard = it.resetCard()
                    repository.insertCard(resetCard)
                }
        clearSelectedCard()
        flippedCards.clear()
        runOnMainThread { isMaxCardsFlipped() }
        isGameOver()
    }

    private fun clearSelectedCard() {
        DEBUG("CLEARING SELECTED CARD")
        selectedCard = null
        DEBUG("SELECTED CARD = $selectedCard")
    }

    private fun isNotSelected(card: Card) =
            !card.isSelected && !card.isMatched

    private fun isValidMatch(card: Card): Boolean {
        DEBUG("SELECTED CARD : $selectedCard \nSECOND CARD : $card")
        DEBUG("isValidMatch = ${selectedCard?.cardId != card.cardId && selectedCard?.spriteId == card.spriteId}")
        return selectedCard?.cardId != card.cardId && selectedCard?.spriteId == card.spriteId
    }

    private fun incrementAttemptCount(): Int {
        attemptCount++
        return attemptCount
    }

    private fun isMaxCardsFlipped() {
        DEBUG("isMaxCardsFlipped = ${flippedCards.size >= MAX_CARD_FLIPS}")
        if (flippedCards.size >= MAX_CARD_FLIPS) {
            broadcastGameState(RESETTING_CARDS)
        } else {
            broadcastGameState(IN_PROGRESS)
        }
    }

    private fun broadcastGameState(state: GameState) {
        gameState.value = state
    }

    private fun delayedAction(function: () -> Unit, delay: Long) {
        disposables.add(
                Completable.timer(delay, TimeUnit.MILLISECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { function() })
    }

    companion object {
        private const val ONE_SECOND: Long = 1000
        private const val MAX_CARD_FLIPS = 2
        private const val DECK_SIZE = 16
        private const val ZERO = 0
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("ON CLEARED CALLED")
        clearDisposables()
    }

    private fun clearDisposables() {
        DEBUG("Clearing Disposables")
        disposables.dispose()
        DEBUG("Disposables cleared : ${disposables.isDisposed}")
    }

}