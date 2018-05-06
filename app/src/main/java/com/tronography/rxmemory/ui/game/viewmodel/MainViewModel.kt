package com.tronography.rxmemory.ui.game.viewmodel

import DEBUG
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.livedata.SingleLiveEvent
import com.tronography.rxmemory.data.repository.Repository
import javax.inject.Inject


class MainViewModel
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
        DEBUG("Initializing MainViewModel")
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("MainViewModel : CLEARED")
    }

}