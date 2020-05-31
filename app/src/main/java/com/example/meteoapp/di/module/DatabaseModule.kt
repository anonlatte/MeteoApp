package com.example.meteoapp.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.meteoapp.db.AppDatabase
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.CityType
import com.example.meteoapp.db.model.Month
import com.example.meteoapp.db.model.Weather
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
class DatabaseModule {

    private lateinit var database: AppDatabase

    @Singleton
    @Provides
    fun providesDatabase(application: Application): AppDatabase {
        database = Room.databaseBuilder(application, AppDatabase::class.java, "meteo-db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch {
                        database.cityDao().insertAll(PREPOPULATE_CITIES)
                        database.weatherDao().insertAll(PREPOPULATE_WEATHER)
                    }
                }
            })
            .build()
        return database
    }

    @Singleton
    @Provides
    fun providesCityDao(database: AppDatabase) = database.cityDao()

    @Singleton
    @Provides
    fun providesFavoriteCityDao(database: AppDatabase) = database.favoriteCityDao()

    @Singleton
    @Provides
    fun providesWeatherDao(database: AppDatabase) = database.weatherDao()


    companion object {
        val PREPOPULATE_CITIES = listOf(
            City(1, "Архангельск", CityType.MEDIUM),
            City(2, "Ревда", CityType.SMALL),
            City(3, "Екатеринбург", CityType.BIG),
            City(4, "Санкт-Петербург", CityType.BIG),
            City(5, "Москва", CityType.BIG)
        )
        val PREPOPULATE_WEATHER = listOf(
            Weather(
                1,
                1,
                mutableMapOf(
                    Month.JANUARY to -15.0,
                    Month.DECEMBER to -10.5,
                    Month.FEBRUARY to -30.1
                )
            ),
            Weather(
                2,
                2,
                mutableMapOf(Month.JUNE to -24.88, Month.JULY to -15.61, Month.AUGUST to 37.79)
            ),
            Weather(
                3,
                3,
                mutableMapOf(
                    Month.OCTOBER to -9.46,
                    Month.NOVEMBER to -2.22,
                    Month.SEPTEMBER to -37.17
                )
            ),
            Weather(
                4,
                4,
                mutableMapOf(
                    Month.FEBRUARY to -28.05,
                    Month.MAY to -31.74,
                    Month.AUGUST to -14.01
                )
            ),
            Weather(
                5,
                5,
                mutableMapOf(Month.JULY to -5.95, Month.JUNE to -9.81, Month.JANUARY to -22.27)
            )
        )
    }
}
