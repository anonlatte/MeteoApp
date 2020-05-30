package com.example.meteoapp.di.module

import com.example.meteoapp.ui.home.HomeFragment
import com.example.meteoapp.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun injectHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun injectSettingsFragment(): SettingsFragment
}