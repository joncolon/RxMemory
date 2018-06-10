package com.tronography.rxmemory.ui.pokedex.viewmodel

import DEBUG
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.livedata.SingleLiveEvent
import com.tronography.rxmemory.data.model.pokemon.Pokemon
import com.tronography.rxmemory.data.repository.Repository
import javax.inject.Inject


class PokedexViewModel
@Inject constructor(
        private val repository: Repository
) : ViewModel() {

    init {
        DEBUG("Initializing PokedexViewModel")
    }

    private val navigateToPokedexDetailsEvent = SingleLiveEvent<Pokemon>()

    val navigateToPokedexDetailsFragment: SingleLiveEvent<Pokemon>
        get() = navigateToPokedexDetailsEvent

    fun getPokemon(): LiveData<List<Pokemon>> {
        return repository.getCaughtPokemon()
    }

    fun onPokemonClicked(pokemon: Pokemon) {
        DEBUG("Pokemon clicked : ${pokemon.name}")
        navigateToPokedexDetailsEvent.value = pokemon
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("PokedexViewModel : CLEARED")
    }

}