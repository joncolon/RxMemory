package com.tronography.rxmemory.ui.home.viewmodel

import DEBUG
import android.arch.lifecycle.ViewModel
import javax.inject.Inject


class HomeActivityViewModel
@Inject constructor() : ViewModel() {

    init {
        DEBUG("Initializing HomeActivityViewModel")
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("HomeActivityViewModel : CLEARED")
    }

}