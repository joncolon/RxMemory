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

        val isMatched: Boolean = false,

        val isSelected: Boolean = false

) : Parcelable {

    fun toMutable() = MutableCard(spriteId, photoUrl, description, cardId, isFlipped, isMatched, isSelected)

    fun matchCard() = Card(spriteId, photoUrl, description, cardId, isFlipped = true, isSelected = false, isMatched = true)

    fun selectCard() = Card(spriteId, photoUrl, description, cardId, isFlipped = true, isSelected = true)

    fun resetCard() = Card(spriteId, photoUrl, description, cardId, isFlipped = false, isSelected = false)
}

