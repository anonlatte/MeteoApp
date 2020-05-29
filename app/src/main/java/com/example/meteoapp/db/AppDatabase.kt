package com.example.meteoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.meteoapp.db.dao.CityDao
import com.example.meteoapp.db.dao.FavoriteCityDao
import com.example.meteoapp.db.dao.WeatherDao
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.FavoriteCity
import com.example.meteoapp.db.model.Weather

@Database(
    entities = [City::class, FavoriteCity::class, Weather::class],
    version = AppDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val VERSION = 1
    }

    abstract fun cityDao(): CityDao
    abstract fun favoriteCityDao(): FavoriteCityDao
    abstract fun weatherDao(): WeatherDao
}