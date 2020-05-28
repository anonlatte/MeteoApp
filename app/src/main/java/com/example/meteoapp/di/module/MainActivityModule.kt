package com.example.meteoapp.di.module

import com.example.meteoapp.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentInjectorsModule::class])
    abstract fun contributeMainActivity(): MainActivity
}