package com.tronography.rxmemory.injection.activitybuilder

import com.tronography.rxmemory.ui.game.activity.GameActivity
import com.tronography.rxmemory.ui.home.activity.HomeActivity
import com.tronography.rxmemory.ui.game.modules.*
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
            MainActivityModule::class,
            HomeFragmentProvider::class
    ))
    internal abstract fun bindMainActivity(): HomeActivity

}