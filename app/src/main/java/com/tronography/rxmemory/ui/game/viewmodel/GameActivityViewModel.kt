package com.tronography.rxmemory.ui.game.viewmodel

import DEBUG
import android.arch.lifecycle.ViewModel
import com.tronography.rxmemory.data.repository.Repository
import javax.inject.Inject


class GameActivityViewModel
@Inject constructor(
        private val repository: Repository
) : ViewModel() {

    init {
        DEBUG("Initializing HomeActivityViewModel")
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("HomeActivityViewModel : CLEARED")
    }

}