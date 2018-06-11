package com.tronography.rxmemory.ui.pokedex.recyclerview

import android.support.v7.util.DiffUtil
import com.tronography.rxmemory.data.model.pokemon.Pokemon


class PokemonDiffCallback(private var newPokemon: List<Pokemon>, private var oldPokemon: List<Pokemon>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldPokemon.size

    override fun getNewListSize(): Int = newPokemon.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPokemon[oldItemPosition] == newPokemon[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPokemon[oldItemPosition] == newPokemon[newItemPosition]
    }

}