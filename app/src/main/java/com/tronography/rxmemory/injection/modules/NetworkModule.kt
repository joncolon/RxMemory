package com.tronography.rxmemory.injection.modules

import com.tronography.rxmemory.data.remote.PokeClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val httpClient = OkHttpClient.Builder()

        httpClient.connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)

        logging.level = HttpLoggingInterceptor.Level.BASIC
        httpClient.addInterceptor(logging)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun providePokeClient(client: OkHttpClient): PokeClient {
        return Retrofit.Builder()
                .baseUrl(PokeClient.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(PokeClient::class.java)
    }
}
