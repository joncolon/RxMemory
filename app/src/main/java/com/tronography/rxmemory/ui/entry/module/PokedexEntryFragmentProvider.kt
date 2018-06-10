package com.tronography.rxmemory.ui.entry.module

import com.tronography.rxmemory.ui.entry.fragment.PokedexEntryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PokedexEntryFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(PokedexEntryFragmentModule::class))
    internal abstract fun providePokedexEntryFragmentFactory(): PokedexEntryFragment

}