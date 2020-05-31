package com.example.meteoapp.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.meteoapp.db.model.FavoriteCity
import org.jetbrains.annotations.TestOnly

@Dao
interface FavoriteCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoriteCity: FavoriteCity): Long

    @Query("SELECT * FROM favorite_city")
    fun getFavoriteCities(): DataSource.Factory<Int, FavoriteCity>

    @Query("SELECT * FROM favorite_city WHERE city_id = :cityId")
    suspend fun getFavoriteCityById(cityId: Long): FavoriteCity?

    @Query("DELETE FROM favorite_city WHERE city_id=:cityId")
    suspend fun removeFromFavorites(cityId: Long): Int

    @TestOnly
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<FavoriteCity>)
}