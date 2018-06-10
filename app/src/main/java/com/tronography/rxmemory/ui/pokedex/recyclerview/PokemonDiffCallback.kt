package com.tronography.rxmemory.ui.pokedex.recyclerview

import android.support.v7.util.DiffUtil
import com.tronography.rxmemory.data.model.pokemon.Pokemon


class PokemonDiffCallback(private var newCards: List<Pokemon>, private var oldCards: List<Pokemon>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldCards.size

    override fun getNewListSize(): Int = newCards.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCards[oldItemPosition] == newCards[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCards[oldItemPosition] == newCards[newItemPosition]
    }

}