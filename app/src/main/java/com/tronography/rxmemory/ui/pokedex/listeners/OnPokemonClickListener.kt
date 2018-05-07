package com.tronography.rxmemory.ui.pokedex.listeners

import com.tronography.rxmemory.data.model.pokemon.Pokemon


interface OnPokemonClickListener {

    fun onPokemonClicked(pokemon: Pokemon)

}