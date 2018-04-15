package com.tronography.rxmemory.data

import DEBUG
import com.tronography.rxmemory.data.http.HttpConstants
import com.tronography.rxmemory.data.http.PokeClient
import com.tronography.rxmemory.data.local.CardDao
import com.tronography.rxmemory.data.local.MutableCard
import com.tronography.rxmemory.data.local.MutableSprite
import com.tronography.rxmemory.data.local.SpriteDao
import com.tronography.rxmemory.data.model.Card
import com.tronography.rxmemory.data.model.Sprite
import com.tronography.rxmemory.data.model.SpriteResponse
import com.tronography.rxmemory.utilities.NetworkConnectivityHelper
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @description: MatchDataRepository is the point of access to all match Data information. Includes
 * cache support, network connectivity awareness, Room  access, and uses RxJava 2 for
 * handling HTTP Requests.
 */
@Singleton
class Repository
@Inject
constructor(
        private val networkConnectivity: NetworkConnectivityHelper,
        private val cardDao: CardDao,
        private val spriteDao: SpriteDao,
        private val client: PokeClient
) {

    fun getAllSprites(): Single<List<Sprite>> {
        return spriteDao.getAllSprites()
    }

    fun getEightRandomSprites(): Single<List<Sprite>> {
        return spriteDao.getEightRandomSprites()
    }

    fun getSpriteById(id: String): Single<Sprite> {
        return spriteDao.getSpriteById(id)
    }

    fun delete(sprite: MutableSprite): Completable {
        return Completable.fromAction {
            spriteDao.delete(sprite)
        }
    }

    fun insert(sprite: MutableSprite): Completable {
        return Completable.fromAction { spriteDao.insert(sprite) }
    }

    fun insert(card: MutableCard): Completable {
        return Completable.fromAction { cardDao.insert(card) }
    }

    fun getCardById(cardId: String): Single<Card> {
        return cardDao.getCardById(cardId)
    }

    fun getEightRandomCards(): Single<List<Card>> {
        return cardDao.getShuffledDeck()
    }

    fun updateCardFlip(cardId: String, isFlipped: Boolean): Completable {
        return Completable.fromAction { cardDao.updateCardFlip(cardId, isFlipped) }
    }

    fun updateCardMatch(cardId: String, isMatched: Boolean) {
        Completable.fromAction { cardDao.updateCardFlip(cardId, isMatched) }
    }

    fun delete(card: MutableCard) {
        Completable.fromAction { cardDao.delete(card) }
    }

    fun deleteCardTable() {
        Completable.fromAction { cardDao.deleteTable() }
    }

    fun getCards(): Single<List<Card>> {
        return cardDao.getAllCards()
    }

    fun getDeck(): Single<List<Card>> {
//        getEightRandomSprites()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .

        return cardDao.getAllCards()
    }

    fun updateFlip(card: Card) {
        return cardDao.updateCardFlip(card.cardId, false)
    }

    fun updateMatch(card: Card, isMatched: Boolean) {
        return cardDao.updateCardMatch(card.cardId, isMatched)
    }

    fun refreshSprites(): Completable {
        return client.getSprites()
                .subscribeOn(Schedulers.io())
                .map { response: SpriteResponse -> response.sprites }
                .doOnSuccess { sprites ->
                    sprites.forEach {
                        updateSpriteDatabase(it)
                    }
                }.toCompletable()

    }

    private fun updateCardDatabase(sprite: Sprite) {
        val photoUrl = HttpConstants.DOMAIN + sprite.image
        val card = Card(sprite.id, photoUrl, sprite.pokemon.name)
        val duplicateCard = Card(sprite.id, photoUrl, sprite.pokemon.name)
        cardDao.insert(card.toMutable())
        cardDao.insert(duplicateCard.toMutable())
    }

    private fun updateSpriteDatabase(sprite: Sprite) {
        DEBUG(sprite.toString())
        spriteDao.insert(sprite.toMutable())
    }

}
