package com.tronography.rxmemory.ui.pokedex.module

import com.tronography.rxmemory.ui.pokedex.fragment.PokedexFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PokedexFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(PokedexFragmentModule::class))
    internal abstract fun providePokedexFragmentFactory(): PokedexFragment

}