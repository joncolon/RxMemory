package com.tronography.rxmemory.ui.game.modules

import com.tronography.rxmemory.ui.game.fragments.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(HomeFragmentModule::class))
    internal abstract fun provideHomeFragmentFactory(): HomeFragment

}