package com.tronography.rxmemory.injection.activitybuilder

import com.tronography.rxmemory.ui.MainActivity
import com.tronography.rxmemory.ui.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    internal abstract fun bindMainActivity(): MainActivity

}