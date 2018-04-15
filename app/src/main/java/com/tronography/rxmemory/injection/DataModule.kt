package com.tronography.rxmemory.injection

import android.arch.persistence.room.Room
import android.content.Context
import com.tronography.rxmemory.data.local.AppDatabase
import com.tronography.rxmemory.data.local.CardDao
import com.tronography.rxmemory.data.local.SpriteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jonat on 4/13/2018.
 */
@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideMovieDao(appDatabase: AppDatabase): CardDao {
        return appDatabase.cardDao()
    }

    @Provides
    @Singleton
    internal fun provideTvShowDao(appDatabase: AppDatabase): SpriteDao {
        return appDatabase.spriteDao()
    }
}