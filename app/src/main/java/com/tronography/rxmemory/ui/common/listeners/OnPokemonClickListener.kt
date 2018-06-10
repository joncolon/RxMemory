package com.tronography.rxmemory.ui.common.listeners

import com.tronography.rxmemory.data.model.pokemon.Pokemon


interface OnPokemonClickListener {

    fun onPokemonClicked(pokemon: Pokemon)

}