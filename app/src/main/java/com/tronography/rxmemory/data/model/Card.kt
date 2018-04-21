package com.tronography.rxmemory.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.tronography.rxmemory.data.local.MutableCard
import kotlinx.android.parcel.Parcelize
import java.util.*


@SuppressLint("ParcelCreator")
@Parcelize
data class Card(

        val spriteId: Int,

        val photoUrl: String,

        val description: String,

        val cardId: String = UUID.randomUUID().toString(),

        val isFlipped: Boolean = false,

        val isMatched: Boolean = false

) : Parcelable {

    fun toMutable(): MutableCard {
        return MutableCard(spriteId, photoUrl, description, cardId, isFlipped, isMatched)
    }

    fun flipCard(flip: Boolean): Card {
        val cardToFlip = MutableCard(spriteId, photoUrl, description, cardId, flip, isMatched)
        return cardToFlip.toImmutable()
    }

    fun matchCard(match: Boolean): Card {
        val cardToMatch = MutableCard(spriteId, photoUrl, description, cardId, isFlipped, match)
        return cardToMatch.toImmutable()
    }

}
