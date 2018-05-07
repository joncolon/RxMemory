package com.tronography.rxmemory.injection.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import com.tronography.rxmemory.injection.annotations.ViewModelKey
import com.tronography.rxmemory.ui.game.viewmodel.GameActivityViewModel
import com.tronography.rxmemory.ui.game.viewmodel.GameViewModel
import com.tronography.rxmemory.ui.home.viewmodel.HomeViewModel
import com.tronography.rxmemory.ui.home.viewmodel.HomeActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    internal abstract fun bindGameViewModel(gameViewModel: GameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    internal abstract fun bindHomeActivityViewModel(homeActivityViewModel: HomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameActivityViewModel::class)
    internal abstract fun bindGameActivityViewModel(gameActivityViewModel: GameActivityViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

}