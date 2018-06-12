package com.tronography.rxmemory.ui.home.viewmodel

import DEBUG
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.livedata.SingleLiveEvent
import com.tronography.rxmemory.data.repository.Repository
import com.tronography.rxmemory.data.state.NetworkStateLiveData
import javax.inject.Inject


class HomeViewModel
@Inject constructor(
        private val repository: Repository) : ViewModel() {

    private val navigateToGameActivityEvent = SingleLiveEvent<String>()
    private val navigateToPokedexActivityEvent = SingleLiveEvent<String>()
    val navigateToGame: SingleLiveEvent<String>
        get() = navigateToGameActivityEvent

    val navigateToPokedex: SingleLiveEvent<String>
        get() = navigateToPokedexActivityEvent

    init {
        DEBUG("Initializing HomeViewModel")
        repository.downloadPokemon()
        repository.deleteCardTable()
    }

    fun getNetworkError(): NetworkStateLiveData {
        return repository.getLiveNetworkError()
    }

    fun getPokemonDatabaseCount() = repository.getPokemonDatabaseCountLiveData()

    fun onPokedexButtonClicked(viewId: String) {
        navigateToPokedexActivityEvent.value = viewId
    }

    fun onPlayButtonClicked(viewId: String) {
        navigateToGameActivityEvent.value = viewId
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("HomeViewModel : CLEARED")
    }
}