package com.tronography.rxmemory.ui.entry.viewmodel

import DEBUG
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.model.pokemon.Pokemon
import javax.inject.Inject


class PokedexEntryViewModel
@Inject constructor(
) : ViewModel() {

    init {
        DEBUG("Initializing PokedexViewModel")
    }

    var pokemon: Pokemon? = null
    val mutablePokemon = MutableLiveData<Pokemon>()

    fun setReceivedPokemon(receivedPokemon: Pokemon) {
        pokemon = receivedPokemon
        pokemon?.let { mutablePokemon.value = pokemon }
    }
    fun observePokemon(): LiveData<Pokemon> {
        pokemon?.let { mutablePokemon.value = pokemon }
        return mutablePokemon
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("PokedexViewModel : CLEARED")
    }

}