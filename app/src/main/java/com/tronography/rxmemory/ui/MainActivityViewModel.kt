package com.tronography.rxmemory.ui

import DEBUG
import ERROR
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.Repository
import com.tronography.rxmemory.data.livedata.GameState
import com.tronography.rxmemory.data.livedata.GameState.*
import com.tronography.rxmemory.data.livedata.NetworkError
import com.tronography.rxmemory.data.model.Card
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by jonat on 4/9/2018.
 */
class MainActivityViewModel @Inject constructor(
        private val repository: Repository) : ViewModel() {

    private var deck: BehaviorSubject<List<Card>> = BehaviorSubject.create()
    private var liveNetworkErrorData: LiveData<NetworkError> = MutableLiveData<NetworkError>()
    private var gameState: BehaviorSubject<GameState> = BehaviorSubject.create()

    private val trackedCards = HashSet<String>()
    private val matchedCards = HashSet<String>()

    private var cardsFlipped: Int
    private var attemptCount: Int
    private var remainingPairs: Int

    private val compositeDisposable = CompositeDisposable()

    init {
        cardsFlipped = 0
        attemptCount = 0
        remainingPairs = 8
        gameState.onNext(NOT_IN_PROGRESS)
    }

    fun onDeckChanged(): Observable<List<Card>> {
        return deck
    }

    fun startGame() {
        initCards()
    }

    fun resetGame() {
        gameState.onNext(NOT_IN_PROGRESS)
        repository.deleteCardTable()
        cardsFlipped = 0
        attemptCount = 0
        remainingPairs = 8
        initCards()
    }

    private fun initCards() {
        compositeDisposable.add(
                repository.refreshSprites()
                        .doOnSubscribe{gameState.onNext(LOADING)}
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{initializeDeck()}
        )
    }

    private fun initializeDeck() {
        compositeDisposable.add(
                repository.getDeck()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ cards: List<Card> ->
                            gameState.onNext(IN_PROGRESS)
                            DEBUG(cards.toString())
                            deck.onNext(cards)
                        }, { error ->
                            gameState.onNext(NOT_IN_PROGRESS)
                            ERROR(error.message.toString())
                        }))
    }

    fun flipCard(card: Card) {
        when (card.isFlipped) {
            true -> repository.updateCardFlip(card.cardId, false)
            false -> repository.updateCardFlip(card.cardId, true)
        }
    }

    fun isFaceDown(card: Card): Boolean {
        return !card.isFlipped && !card.isMatched
    }

    fun isGameOver(): Boolean {
        return remainingPairs == 0
    }

    fun markCardAsMatched(card: Card) {
        repository.updateMatch(card, true)
    }

    fun untrackFlippedCards() {
        trackedCards.clear()
    }

    fun incrementAttemptCount(): Int {
        attemptCount++
        return attemptCount
    }

    fun delayedCardUpdate() {
        Completable.timer(ONE_AND_A_HALF_SECONDS.toLong(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { this.resetFlipCount() }
    }

    fun matchFound(card: Card): Boolean {
        return trackedCards.contains(card.photoUrl)
    }

    fun resetFlipCount() {
        cardsFlipped = 0
    }

    fun matchNotFound(currentCard: Card): Boolean {
        return currentCard.isFlipped && !currentCard.isMatched
    }

    fun cardPreviouslyMatched(currentCard: Card): Boolean {
        return matchedCards.contains(currentCard.photoUrl)
    }

    companion object {
        private const val ONE_AND_A_HALF_SECONDS = 1500
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("ON CLEARED CALLED")
        compositeDisposable.dispose()
        repository.deleteCardTable()
    }
}