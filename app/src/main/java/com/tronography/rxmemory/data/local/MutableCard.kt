package com.tronography.rxmemory.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tronography.rxmemory.data.model.cards.Card


@Entity(tableName = AppDatabase.CARD_TABLE)
class MutableCard(

        var pokemonId: Int,

        var photoUrl: String,

        var description: String,

        @PrimaryKey()
        var cardId: Long,

        var isFlipped: Boolean,

        var isMatched: Boolean,

        var isSelected: Boolean

) {
    fun toImmutable(): Card {
        return Card(
                pokemonId,
                photoUrl,
                description,
                cardId,
                isFlipped,
                isMatched,
                isSelected
        )
    }
}