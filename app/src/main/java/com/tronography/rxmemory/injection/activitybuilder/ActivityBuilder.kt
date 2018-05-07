package com.tronography.rxmemory.injection.activitybuilder

import com.tronography.rxmemory.ui.game.activity.GameActivity
import com.tronography.rxmemory.ui.home.activity.HomeActivity
import com.tronography.rxmemory.ui.game.modules.*
import com.tronography.rxmemory.ui.home.module.HomeFragmentProvider
import com.tronography.rxmemory.ui.home.module.HomeActivityModule
import com.tronography.rxmemory.ui.pokedex.activity.PokedexActivity
import com.tronography.rxmemory.ui.pokedex.module.PokedexActivityModule
import com.tronography.rxmemory.ui.pokedex.module.PokedexFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(
            GameActivityModule::class,
            GameFragmentProvider::class,
            GameOverFragmentProvider::class))
    internal abstract fun bindGameActivity(): GameActivity

    @ContributesAndroidInjector(modules = arrayOf(
            HomeActivityModule::class,
            HomeFragmentProvider::class
    ))
    internal abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = arrayOf(
            PokedexActivityModule::class,
            PokedexFragmentProvider::class
    ))
    internal abstract fun bindPokedexActivity(): PokedexActivity

}