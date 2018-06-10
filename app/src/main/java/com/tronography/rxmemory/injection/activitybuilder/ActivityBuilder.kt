package com.tronography.rxmemory.injection.activitybuilder

import com.tronography.rxmemory.ui.entry.module.PokedexEntryFragmentModule
import com.tronography.rxmemory.ui.entry.module.PokedexEntryFragmentProvider
import com.tronography.rxmemory.ui.game.modules.GameFragmentProvider
import com.tronography.rxmemory.ui.gameover.module.GameOverFragmentProvider
import com.tronography.rxmemory.ui.home.activity.HomeActivity
import com.tronography.rxmemory.ui.home.module.HomeActivityModule
import com.tronography.rxmemory.ui.home.module.HomeFragmentProvider
import com.tronography.rxmemory.ui.pokedex.module.PokedexFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(
            HomeActivityModule::class,
            HomeFragmentProvider::class,
            PokedexFragmentProvider::class,
            PokedexEntryFragmentModule::class,
            PokedexEntryFragmentProvider::class,
            GameFragmentProvider::class,
            GameOverFragmentProvider::class
    ))
    internal abstract fun bindHomeActivity(): HomeActivity

}