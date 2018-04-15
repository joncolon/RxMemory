package com.tronography.rxmemory.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import com.tronography.rxmemory.data.model.Card
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface CardDao {

    @Query("SELECT * FROM ${AppDatabase.CARD_TABLE}")
    fun getAllCards(): Single<List<Card>>

    @Query("SELECT * FROM ${AppDatabase.CARD_TABLE}")
    fun getShuffledDeck(): Single<List<Card>>

    @Query("SELECT * FROM ${AppDatabase.CARD_TABLE} WHERE cardId = :cardId")
    fun getCardById(cardId: String): Single<Card>

    @Query("UPDATE ${AppDatabase.CARD_TABLE} SET isFlipped = :isFlipped WHERE cardId = :cardId")
    fun updateCardFlip(cardId: String, isFlipped: Boolean)

    @Query("UPDATE ${AppDatabase.CARD_TABLE} SET isMatched = :isMatched WHERE cardId = :cardId")
    fun updateCardMatch(cardId: String, isMatched: Boolean)

    @Query("DELETE FROM ${AppDatabase.CARD_TABLE}")
    fun deleteTable()

    @Delete
    fun delete(card: MutableCard)

    @Insert(onConflict = IGNORE)
    fun insert(card: MutableCard)

}