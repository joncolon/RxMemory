package com.tronography.rxmemory.data.repository


import DEBUG
import android.arch.lifecycle.LiveData
import com.tronography.rxmemory.data.http.HttpConstants
import com.tronography.rxmemory.data.http.PokeClient
import com.tronography.rxmemory.data.local.CardDao
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

    fun deleteCardTable(): Observable<Unit>? {
        return Observable.fromCallable { cardDao.deleteTable() }
                .subscribeOn(Schedulers.io())
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
        deleteCardTable()
                ?.flatMap { return@flatMap getSpritesFromDB() }
                ?.flatMap { sprites ->
                    if (sprites.isEmpty()) {
                        return@flatMap getSpritesFromAPI()
                    } else
                        return@flatMap just(sprites)
                }?.subscribeWith(object : DisposableObserver<List<Sprite>>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: List<Sprite>) {
                        executeInThread { updateCardDatabase(t) }
                    }

                    override fun onError(e: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }


    private fun getSpritesFromAPI(): Observable<List<Sprite>> {
        return client.getSprites()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .map { response -> response.sprites }
                .concatMap { sprites -> fromCallable { updateSpriteDatabase(sprites) } }
                .switchMap { t -> getSpritesFromDB() }
    }

    private fun getSpritesFromDB(): Observable<List<Sprite>> {
        return spriteDao.getEightRandomSprites()
                .toObservable()
                .doOnNext { DEBUG("Dispatching ${it.size} sprites from DB...") }
    }

    private fun updateCardDatabase(sprites: List<Sprite>) {
        DEBUG("Saving ${sprites.size} Cards to DB")
        sprites.forEach {
            val photoUrl = HttpConstants.DOMAIN + it.image
            val card = Card(it.id, photoUrl, it.pokemon.name)
            val duplicateCard = Card(it.id, photoUrl, it.pokemon.name)

            cardDao.insert(card.toMutable())
            cardDao.insert(duplicateCard.toMutable())

            DEBUG("Inserted $card into Card DB")
            DEBUG("Inserted $duplicateCard into Card DB")
        }
    }

    private fun updateSpriteDatabase(sprites: List<Sprite>) {
        sprites.forEach { sprite ->
            spriteDao.insert(sprite.toMutable())
            DEBUG("Inserted $sprite into Card DB")
        }
    }

}
