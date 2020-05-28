package com.example.meteoapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.meteoapp.db.model.City
import org.jetbrains.annotations.TestOnly

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createCity(city: City): Long

    @Query("SELECT * FROM city")
    fun getCities(): LiveData<List<City>>

    @Query("SELECT * FROM city WHERE name = :name")
    fun getCityByName(name: String): LiveData<City>

    @Update
    fun updateCity(city: City): Int

    @Delete
    suspend fun deleteCity(city: City): Int

    @TestOnly
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<City>)
}