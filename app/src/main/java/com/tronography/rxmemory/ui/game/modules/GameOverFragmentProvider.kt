package com.tronography.rxmemory.ui.game.modules

import com.tronography.rxmemory.ui.game.fragments.GameOverFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class GameOverFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(GameOverFragmentModule::class))
    internal abstract fun provideGameFragmentFactory(): GameOverFragment

}