package com.example.meteoapp.repository

import com.example.meteoapp.db.dao.CityDao
import com.example.meteoapp.db.dao.WeatherDao
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.Weather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
        private var cityDao: CityDao,
        private var weatherDao: WeatherDao
) {
    suspend fun createCity(city: City) = cityDao.createCity(city)
    fun getCities() = cityDao.getCities()
    fun getCityByName(name: String) = cityDao.getCityByName(name)
    fun updateCity(city: City) = cityDao.updateCity(city)
    suspend fun deleteCity(city: City) = cityDao.deleteCity(city)

    suspend fun createWeather(weather: Weather) = weatherDao.createWeather(weather)
    fun getWeatherByCity(cityId: Long) = weatherDao.getWeatherByCity(cityId)
    fun getAllWeatherRows(weather: Weather) = weatherDao.updateWeather(weather)
    suspend fun updateWeather(weather: Weather) = weatherDao.deleteWeather(weather)
    fun deleteWeather(weather: Weather) = weatherDao.updateWeather(weather)
    suspend fun insertAll(weather: Weather) = weatherDao.deleteWeather(weather)
}