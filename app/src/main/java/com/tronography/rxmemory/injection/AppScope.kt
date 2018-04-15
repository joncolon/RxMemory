package com.tronography.rxmemory.injection

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.net.ConnectivityManager
import com.tronography.rxmemory.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppScope {

    @Provides
    @Singleton
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
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

