package com.example.meteoapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.meteoapp.db.model.Weather
import org.jetbrains.annotations.TestOnly

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createWeather(weather: Weather): Long

    @Query("SELECT * FROM weather WHERE city_id = :cityId")
    fun getWeatherByCity(cityId: Int): LiveData<Weather>

    @Query("SELECT * FROM weather")
    fun getAllWeatherRows(): LiveData<List<Weather>>

    @Update
    fun updateWeather(weather: Weather): Int

    @Delete
    fun deleteWeather(weather: Weather)

    @TestOnly
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weatherOfCities: List<Weather>)
}