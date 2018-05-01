package com.tronography.rxmemory.injection.activitybuilder

import com.tronography.rxmemory.ui.game.activity.GameActivity
import com.tronography.rxmemory.ui.game.modules.GameOverFragmentProvider
import com.tronography.rxmemory.ui.game.modules.GameActivityModule
import com.tronography.rxmemory.ui.game.modules.GameFragmentProvider
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