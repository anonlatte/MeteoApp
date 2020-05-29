package com.example.meteoapp.di.module

import com.example.meteoapp.db.dao.CityDao
import com.example.meteoapp.db.dao.FavoriteCityDao
import com.example.meteoapp.db.dao.WeatherDao
import com.example.meteoapp.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun providesRepository(
        cityDao: CityDao,
        favoriteCityDao: FavoriteCityDao,
        weatherDao: WeatherDao
    ) =
        Repository(cityDao, favoriteCityDao, weatherDao)
}