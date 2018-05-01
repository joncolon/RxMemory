package com.tronography.rxmemory.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.tronography.rxmemory.data.model.cards.Card
import io.reactivex.Single


@Dao
abstract class CardDao {

    @Query("SELECT * FROM ${AppDatabase.CARD_TABLE} ORDER BY cardId")
    abstract fun getAllCards(): LiveData<List<Card>>

    @Query("SELECT * FROM ${AppDatabase.CARD_TABLE} WHERE cardId = :cardId")
    abstract fun getCardById(cardId: String): Single<Card>

    @Query("UPDATE ${AppDatabase.CARD_TABLE} SET isFlipped = :isFlipped WHERE cardId = :cardId")
    abstract fun updateCardFlip(cardId: String, isFlipped: Boolean)

    @Query("UPDATE ${AppDatabase.CARD_TABLE} SET isMatched = :isMatched WHERE cardId = :cardId")
    abstract fun updateCardMatch(cardId: String, isMatched: Boolean)

    @Query("DELETE FROM ${AppDatabase.CARD_TABLE}")
    abstract fun deleteTable()

    @Delete
    abstract fun delete(card: MutableCard)

    @Insert(onConflict = REPLACE)
    abstract fun insert(card: MutableCard)

    @Insert(onConflict = REPLACE)
    abstract fun insertAll(cards: List<MutableCard>)

    @Transaction
    open fun repopulateTable(cards: List<MutableCard>) {
        deleteTable()
        insertAll(cards)
    }

}