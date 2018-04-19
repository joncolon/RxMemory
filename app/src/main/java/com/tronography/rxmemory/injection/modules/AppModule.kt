package com.tronography.rxmemory.injection.modules

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

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

}

