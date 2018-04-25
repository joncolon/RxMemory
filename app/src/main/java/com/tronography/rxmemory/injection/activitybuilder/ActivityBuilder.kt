package com.tronography.rxmemory.injection.activitybuilder

import com.tronography.rxmemory.ui.pokedex.GameActivity
import com.tronography.rxmemory.ui.pokedex.gameover.GameOverFragmentProvider
import com.tronography.rxmemory.ui.pokedex.module.GameActivityModule
import com.tronography.rxmemory.ui.pokedex.module.GameFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(
            GameActivityModule::class,
            GameFragmentProvider::class,
            GameOverFragmentProvider::class
    ))
    internal abstract fun bindGameActivity(): GameActivity

}