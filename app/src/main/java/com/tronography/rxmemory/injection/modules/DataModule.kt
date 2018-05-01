package com.tronography.rxmemory.injection.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.tronography.rxmemory.data.local.AppDatabase
import com.tronography.rxmemory.data.local.CardDao
import com.tronography.rxmemory.data.local.PokemonDao
import com.tronography.rxmemory.injection.annotations.DatabaseInfo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideCardDao(appDatabase: AppDatabase): CardDao {
        return appDatabase.cardDao()
    }

    @Provides
    @Singleton
    internal fun providePokemonDao(appDatabase: AppDatabase): PokemonDao {
        return appDatabase.pokemonDao()
    }

    @Provides
    @DatabaseInfo
    internal fun provideDatabaseName(): String {
        return AppDatabase.FILENAME
    }

    @Provides
    @Singleton
    internal fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName).fallbackToDestructiveMigration()
                .build()
    }
}