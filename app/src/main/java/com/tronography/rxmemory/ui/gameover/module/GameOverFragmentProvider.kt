package com.tronography.rxmemory.ui.gameover.module

import com.tronography.rxmemory.ui.gameover.GameOverFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class GameOverFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(GameOverFragmentModule::class))
    internal abstract fun provideGameFragmentFactory(): GameOverFragment

}