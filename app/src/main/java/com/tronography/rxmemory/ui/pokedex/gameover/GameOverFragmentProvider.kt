package com.tronography.rxmemory.ui.pokedex.gameover

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class GameOverFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(GameOverFragmentModule::class))
    internal abstract fun provideGameFragmentFactory(): GameOverFragment

}