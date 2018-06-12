package com.tronography.rxmemory.ui.pokedex.viewmodel

import DEBUG
import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.livedata.SingleLiveEvent
import com.tronography.rxmemory.data.model.pokemon.PokemonData
import com.tronography.rxmemory.data.repository.Repository
import javax.inject.Inject


class PokedexViewModel
@Inject constructor(
        private val repository: Repository
) : ViewModel() {

    init {
        DEBUG("Initializing PokedexViewModel")
    }

    private val selectedPokemon = SingleLiveEvent<PokemonData>()

    val getSelectedPokemon: SingleLiveEvent<PokemonData>
        get() = selectedPokemon

    var pokemon: PokemonData? = null

    fun getPokemon(): LiveData<List<PokemonData>> {
        return repository.getCaughtPokemon()
    }

    @SuppressLint("CheckResult")
    fun onPokemonClicked(pokemon: PokemonData) {
        DEBUG("PokemonResponse clicked : ${pokemon.name}")
        this.pokemon = pokemon
        DEBUG("GETTING SPECIES...")

        selectedPokemon.value = pokemon
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("PokedexViewModel : CLEARED")
    }

}