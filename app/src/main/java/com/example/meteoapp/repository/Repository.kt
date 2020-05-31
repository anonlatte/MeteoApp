package com.example.meteoapp.repository

import com.example.meteoapp.db.dao.CityDao
import com.example.meteoapp.db.dao.FavoriteCityDao
import com.example.meteoapp.db.dao.WeatherDao
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.FavoriteCity
import com.example.meteoapp.db.model.Weather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private var cityDao: CityDao,
    private var favoriteCityDao: FavoriteCityDao,
    private var weatherDao: WeatherDao
) {
    suspend fun createCity(city: City) = cityDao.createCity(city)
    fun getCities() = cityDao.getCities()
    fun getCityByName(name: String) = cityDao.getCityByName(name)
    fun updateCity(city: City) = cityDao.updateCity(city)
    suspend fun deleteCity(city: City) = cityDao.deleteCity(city)
    suspend fun insertAllCities(cities: List<City>) = cityDao.insertAll(cities)

    suspend fun addToFavorites(favoriteCity: FavoriteCity) =
        favoriteCityDao.addToFavorites(favoriteCity)

    fun getFavoriteCities() = favoriteCityDao.getFavoriteCities()
    suspend fun getFavoriteCityById(cityId: Long) = favoriteCityDao.getFavoriteCityById(cityId)
    suspend fun removeFromFavorites(cityId: Long) =
        favoriteCityDao.removeFromFavorites(cityId)

    suspend fun insertAllFavoriteCities(favoriteCity: List<FavoriteCity>) =
        favoriteCityDao.insertAll(favoriteCity)

    suspend fun createWeather(weather: Weather) = weatherDao.createWeather(weather)
    fun getWeatherByCity(cityId: Long) = weatherDao.getWeatherByCity(cityId)
    fun getAllWeatherRows() = weatherDao.getAllWeatherRows()
    suspend fun updateWeather(weather: Weather) = weatherDao.deleteWeather(weather)
    fun deleteWeather(weather: Weather) = weatherDao.updateWeather(weather)
    suspend fun insertAllCitiesWeather(weather: List<Weather>) = weatherDao.insertAll(weather)
}