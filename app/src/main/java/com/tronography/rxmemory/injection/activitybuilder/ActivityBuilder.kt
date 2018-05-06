package com.tronography.rxmemory.injection.activitybuilder

import com.tronography.rxmemory.ui.game.activity.MainActivity
import com.tronography.rxmemory.ui.game.modules.GameActivityModule
import com.tronography.rxmemory.ui.game.modules.GameFragmentProvider
import com.tronography.rxmemory.ui.game.modules.GameOverFragmentProvider
import com.tronography.rxmemory.ui.game.modules.HomeFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(
            GameActivityModule::class,
            GameFragmentProvider::class,
            GameOverFragmentProvider::class,
            HomeFragmentProvider::class
    ))
    internal abstract fun bindGameActivity(): MainActivity

}