package com.example.meteoapp.di.module

import com.example.meteoapp.db.dao.CityDao
import com.example.meteoapp.db.dao.WeatherDao
import com.example.meteoapp.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun providesRepository(cityDao: CityDao, weatherDao: WeatherDao) =
        Repository(cityDao, weatherDao)
}