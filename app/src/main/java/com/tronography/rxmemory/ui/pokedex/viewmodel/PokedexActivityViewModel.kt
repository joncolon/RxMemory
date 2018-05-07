package com.tronography.rxmemory.ui.pokedex.viewmodel

import DEBUG
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.repository.Repository
import javax.inject.Inject


class PokedexActivityViewModel
@Inject constructor(
        private val repository: Repository
) : ViewModel() {

    init {
        DEBUG("Initializing PokedexActivityViewModel")
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("PokedexActivityViewModel : CLEARED")
    }

}