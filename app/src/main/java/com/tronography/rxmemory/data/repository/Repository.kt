package com.tronography.rxmemory.data.repository


import DEBUG
import ERROR
import android.arch.lifecycle.LiveData
import com.tronography.rxmemory.data.http.HttpConstants
import com.tronography.rxmemory.data.http.PokeClient
import com.tronography.rxmemory.data.local.CardDao
import com.tronography.rxmemory.data.local.MutableCard
import com.tronography.rxmemory.data.local.SpriteDao
import com.tronography.rxmemory.data.model.Card
import com.tronography.rxmemory.data.model.Sprite
import com.tronography.rxmemory.utilities.NetworkConnectivityHelper
import executeInThread
import io.reactivex.Observable
import io.reactivex.Observable.fromCallable
import io.reactivex.Observable.just
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository
@Inject
constructor(

        private val networkConnectivity: NetworkConnectivityHelper,

        private val cardDao: CardDao,

        private val spriteDao: SpriteDao,

        private val client: PokeClient

) {

    fun deleteCardTable(): Observable<Unit> {
        return Observable.fromCallable { cardDao.deleteTable() }
    }

    fun updateMatch(card: Card, isMatched: Boolean) {
        return executeInThread { cardDao.updateCardMatch(card.cardId, isMatched) }
    }

    fun updateCardFlip(card: Card, isFlipped: Boolean) {
        executeInThread { cardDao.updateCardFlip(card.cardId, isFlipped) }
    }

    fun insertCard(card: Card) {
        executeInThread { cardDao.insert(card.toMutable()) }
    }

    fun getCards(): LiveData<List<Card>> {
        createNewDeck()
        return cardDao.getAllCards()
    }

    fun createNewDeck() {
        getSpritesFromDB()
                .subscribeOn(Schedulers.io())
                .flatMap { sprites ->
                    if (sprites.isEmpty()) {
                        return@flatMap getSpritesFromAPI()
                    } else
                        return@flatMap Observable.just(sprites)
                }
                .concatMap { sprites -> Observable.just<Unit>(updateCardDatabase(sprites)) }
                .subscribeWith(object : DisposableObserver<Unit>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: Unit) {
                    }

                    override fun onError(e: Throwable) {
                        ERROR("Unable to create a new deck : \n${e.cause} \n${e.message}")
                    }
                })
    }


    private fun getSpritesFromAPI(): Observable<List<Sprite>> {
        return client.getSprites()
                .toObservable()
                .map { response -> response.sprites }
                .concatMap { sprites -> fromCallable { updateSpriteDatabase(sprites) } }
                .switchMap { getSpritesFromDB() }
    }

    private fun getSpritesFromDB(): Observable<List<Sprite>> {
        return spriteDao.getEightRandomSprites()
                .toObservable()
                .doOnNext { DEBUG("Dispatching ${it.size} sprites from DB...") }
    }

    private fun updateCardDatabase(sprites: List<Sprite>) {
        DEBUG("Saving ${sprites.size} Cards to DB")
        val cards = mutableListOf<MutableCard>()
        sprites.forEach {
            val photoUrl = HttpConstants.DOMAIN + it.image
            val card = Card(it.id, photoUrl, it.pokemon.name)
            val duplicateCard = Card(it.id, photoUrl, it.pokemon.name)

            cards.add(card.toMutable())
            cards.add(duplicateCard.toMutable())

        }

        cardDao.repopulateTable(cards)
    }

    private fun updateSpriteDatabase(sprites: List<Sprite>) {
        sprites.forEach { sprite ->
            spriteDao.insert(sprite.toMutable())
            DEBUG("Inserted $sprite into Card DB")
        }
    }

}
