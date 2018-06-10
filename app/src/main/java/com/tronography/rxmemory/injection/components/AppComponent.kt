package com.tronography.rxmemory.injection.components

import android.app.Application
import com.tronography.rxmemory.application.MemoryApp
import com.tronography.rxmemory.injection.activitybuilder.ActivityBuilder
import com.tronography.rxmemory.injection.modules.AppModule
import com.tronography.rxmemory.injection.modules.DataModule
import com.tronography.rxmemory.injection.modules.NetworkModule
import com.tronography.rxmemory.injection.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        dagger.android.AndroidInjectionModule::class,
        AppModule::class,
        DataModule::class,
        ActivityBuilder::class,
        NetworkModule::class,
        ViewModelModule::class
))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: MemoryApp)

}