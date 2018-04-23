package com.tronography.rxmemory.injection.activitybuilder

import com.tronography.rxmemory.ui.game.GameActivity
import com.tronography.rxmemory.ui.game.module.GameActivityModule
import com.tronography.rxmemory.ui.game.module.GameFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(GameActivityModule::class, GameFragmentProvider::class))
    internal abstract fun bindGameActivity(): GameActivity

}