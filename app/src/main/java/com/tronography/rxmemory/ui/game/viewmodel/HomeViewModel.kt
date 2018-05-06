package com.tronography.rxmemory.ui.game.viewmodel

import DEBUG
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.livedata.SingleLiveEvent
import com.tronography.rxmemory.data.repository.Repository
import javax.inject.Inject


class HomeViewModel
@Inject constructor(
        private val repository: Repository
) : ViewModel() {

    private val navigateToGameFragmentEvent = SingleLiveEvent<String>()
    private val navigateToSettingsFragmentEvent = SingleLiveEvent<String>()
    private val navigateToPokedexFragmentEvent = SingleLiveEvent<String>()

    val navigateToGameFragment : SingleLiveEvent<String>
        get() = navigateToGameFragmentEvent

    val navigateToSettingsFragment : SingleLiveEvent<String>
        get() = navigateToSettingsFragmentEvent

    val navigateToPokedexFragment : SingleLiveEvent<String>
        get() = navigateToPokedexFragmentEvent

    init {
        DEBUG("Initializing HomeViewModel")
    }

    private fun onSettingsButtonClicked(viewId: String) {
        navigateToSettingsFragmentEvent.value = viewId
    }

    private fun onPokedexButtonClicked(viewId: String) {
        navigateToPokedexFragmentEvent.value = viewId
    }

    fun onPlayButtonClicked(viewId: String) {
        navigateToGameFragmentEvent.value = viewId
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("HomeViewModel : CLEARED")
    }
}