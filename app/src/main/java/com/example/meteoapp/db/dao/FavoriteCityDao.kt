package com.example.meteoapp.db.dao

import androidx.paging.DataSource
import androidx.room.*
import com.example.meteoapp.db.model.FavoriteCity
import org.jetbrains.annotations.TestOnly

@Dao
interface FavoriteCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoriteCity: FavoriteCity): Long

    @Query("SELECT * FROM favorite_city")
    fun getFavoriteCities(): DataSource.Factory<Int, FavoriteCity>

    @Query("SELECT * FROM favorite_city WHERE city_id = :cityId")
    fun getFavoriteCityById(cityId: Long): DataSource.Factory<Int, FavoriteCity>

    @Delete
    suspend fun removeFromFavorites(favoriteCity: FavoriteCity): Int

    @TestOnly
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<FavoriteCity>)
}