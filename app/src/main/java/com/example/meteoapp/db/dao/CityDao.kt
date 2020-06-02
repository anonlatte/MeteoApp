package com.example.meteoapp.db.dao

import androidx.paging.DataSource
import androidx.room.*
import com.example.meteoapp.db.model.City
import org.jetbrains.annotations.TestOnly

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createCity(city: City): Long

    @Query("SELECT city.* FROM city LEFT JOIN favorite_city ON city.id=favorite_city.city_id GROUP BY city.id ORDER BY favorite_city.city_id DESC")
    fun getCities(): DataSource.Factory<Int, City>

    @Query("SELECT * FROM city WHERE id = :id")
    fun getCityById(id: Long): DataSource.Factory<Int, City>

    @Query("SELECT * FROM city WHERE name = :name")
    fun getCityByName(name: String): DataSource.Factory<Int, City>

    @Update
    fun updateCity(city: City): Int

    @Delete
    suspend fun deleteCity(city: City): Int

    @TestOnly
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<City>)
}