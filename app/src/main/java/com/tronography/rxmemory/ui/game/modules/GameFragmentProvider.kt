package com.tronography.rxmemory.ui.game.modules

import com.tronography.rxmemory.ui.game.fragments.GameFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class GameFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(GameFragmentModule::class))
    internal abstract fun provideGameFragmentFactory(): GameFragment

}