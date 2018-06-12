package com.tronography.rxmemory.ui.common.listeners

import com.tronography.rxmemory.data.model.pokemon.PokemonData


interface OnPokemonClickListener {

    fun onPokemonClicked(pokemon: PokemonData)

}