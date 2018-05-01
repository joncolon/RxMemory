package com.tronography.rxmemory.utilities

import android.support.v7.util.DiffUtil
import com.tronography.rxmemory.data.model.cards.Card


class DiffCallback(private var newCards: List<Card>, private var oldCards: List<Card>) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldCards.size
    }

    override fun getNewListSize(): Int {
        return newCards.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCards[oldItemPosition].pokemonId == newCards[newItemPosition].pokemonId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCards[oldItemPosition].isFlipped == newCards[newItemPosition].isFlipped
    }

}