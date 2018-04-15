package com.tronography.rxmemory.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tronography.rxmemory.data.model.Card
import java.util.*


@Entity(tableName = AppDatabase.CARD_TABLE)
class MutableCard (
        var spriteId: Int,

        var photoUrl: String,

        var description: String,

        @PrimaryKey
        var cardId: String = UUID.randomUUID().toString(),

        var isFlipped: Boolean,

        var isMatched: Boolean
) {
    fun toImmutable(): Card {
        return Card(spriteId, photoUrl, description, cardId, isFlipped, isMatched)
    }
}