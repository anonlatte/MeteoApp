package com.example.meteoapp.db.dao

import androidx.paging.DataSource
import androidx.room.*
import com.example.meteoapp.db.model.Weather
import org.jetbrains.annotations.TestOnly

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createWeather(weather: Weather): Long

    @Query("SELECT * FROM weather WHERE city_id = :cityId")
    suspend fun getWeatherByCity(cityId: Long): Weather

    @Query("SELECT * FROM weather")
    fun getAllWeatherRows(): DataSource.Factory<Int, Weather>

    @Update
    fun updateWeather(weather: Weather): Int

    @Delete
    suspend fun deleteWeather(weather: Weather): Int

    @TestOnly
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weatherOfCities: List<Weather>)
}