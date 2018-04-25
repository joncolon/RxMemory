package com.tronography.rxmemory.ui.pokedex.module

import com.tronography.rxmemory.ui.pokedex.GameFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class GameFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(GameFragmentModule::class))
    internal abstract fun provideGameFragmentFactory(): GameFragment

}