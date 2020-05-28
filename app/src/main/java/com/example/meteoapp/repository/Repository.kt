package com.example.meteoapp.repository

import com.example.meteoapp.db.dao.CityDao
import com.example.meteoapp.db.dao.WeatherDao
import com.example.meteoapp.db.model.City
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private var cityDao: CityDao,
    private var weatherDao: WeatherDao
) {
    suspend fun createCity(city: City) = cityDao.createCity(city)
    fun findCityByName(name: String) = cityDao.getCityByName(name)
    fun updateCity(city: City) = cityDao.updateCity(city)
    suspend fun deleteCity(city: City) = cityDao.deleteCity(city)
}