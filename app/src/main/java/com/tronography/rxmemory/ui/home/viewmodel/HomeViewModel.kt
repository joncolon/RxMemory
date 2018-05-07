package com.tronography.rxmemory.ui.home.viewmodel

import DEBUG
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.livedata.SingleLiveEvent
import com.tronography.rxmemory.data.repository.Repository
import javax.inject.Inject


class HomeViewModel
@Inject constructor(
        private val repository: Repository) : ViewModel() {

    private val navigateToGameActivityEvent = SingleLiveEvent<String>()
    private val navigateToSettingsFragmentEvent = SingleLiveEvent<String>()
    private val navigateToPokedexActivityEvent = SingleLiveEvent<String>()

    val navigateToGameActivity : SingleLiveEvent<String>
        get() = navigateToGameActivityEvent

    val navigateToSettingsFragment : SingleLiveEvent<String>
        get() = navigateToSettingsFragmentEvent

    val navigateToPokedexActivity : SingleLiveEvent<String>
        get() = navigateToPokedexActivityEvent

    init {
        DEBUG("Initializing HomeViewModel")
        repository.deleteCardTable()
    }

    private fun onSettingsButtonClicked(viewId: String) {
        navigateToSettingsFragmentEvent.value = viewId
    }

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