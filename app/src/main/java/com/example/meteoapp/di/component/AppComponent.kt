package com.example.meteoapp.di.component

import android.app.Application
import com.example.meteoapp.MeteoApp
import com.example.meteoapp.di.module.AppModule
import com.example.meteoapp.di.module.DatabaseModule
import com.example.meteoapp.di.module.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, MainActivityModule::class, DatabaseModule::class])
interface AppComponent {

    fun inject(meteoApp: MeteoApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}