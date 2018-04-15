package com.tronography.rxmemory.injection

import android.app.Application
import com.tronography.rxmemory.application.MemoryApp
import com.tronography.rxmemory.injection.activitybuilder.ActivityBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppScope::class,
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