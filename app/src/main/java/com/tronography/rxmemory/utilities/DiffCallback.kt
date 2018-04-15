package com.tronography.rxmemory.utilities

import android.support.v7.util.DiffUtil
import com.tronography.rxmemory.data.model.Card


class DiffCallback(internal var newCards: List<Card>, internal var oldCards: List<Card>) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldCards.size
    }

    override fun getNewListSize(): Int {
        return newCards.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCards[oldItemPosition].equals(newCards[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCards[oldItemPosition].equals(newCards[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}