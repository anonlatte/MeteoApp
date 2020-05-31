package com.example.meteoapp.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.toLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.meteoapp.db.AppDatabase
import com.example.meteoapp.db.dao.CityDao
import com.example.meteoapp.db.dao.WeatherDao
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.CityType
import com.example.meteoapp.db.model.Month
import com.example.meteoapp.db.model.Weather
import com.example.meteoapp.utils.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {
    private lateinit var weatherDao: WeatherDao
    private lateinit var cityDao: CityDao
    private lateinit var db: AppDatabase
    private var testWeatherId: Long = -1
    private val city = City(7, "Рязань", CityType.SMALL)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        weatherDao = db.weatherDao()
        cityDao = db.cityDao()

        cityDao.insertAll(testCities)
        weatherDao.insertAll(testWeatherOfCities)

        cityDao.createCity(testCity)
        cityDao.createCity(city)

        testWeatherId = weatherDao.createWeather(testWeather)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetAllWeatherRows() = runBlocking {
        val weather = Weather(
                7, 7, mutableMapOf(
                Month.DECEMBER to -15.4214042, Month.OCTOBER to -10.5, Month.NOVEMBER to -30.1,
                Month.MARCH to -15.124012, Month.MAY to -10.474575, Month.AUGUST to -30.1
        )
        )
        weatherDao.createWeather(weather)
        MatcherAssert.assertThat(
            getValue(weatherDao.getAllWeatherRows().toLiveData(50)).size,
            Matchers.equalTo(7)
        )
    }

    @Test
    fun testGetWeatherByCity() = runBlocking {
        val weather = weatherDao.getWeatherByCity(testWeatherId)
        MatcherAssert.assertThat(
            weather.cityId.toLong(),
            Matchers.equalTo(testWeatherId)
        )
    }

    @Test
    fun testUpdateWeather() = runBlocking {
        testWeather.weatherMap[Month.FEBRUARY] = -28.4
        MatcherAssert.assertThat(weatherDao.updateWeather(testWeather), Matchers.equalTo(1))
    }

    @Test
    fun testDeleteWeather() = runBlocking {
        MatcherAssert.assertThat(weatherDao.deleteWeather(testWeather), Matchers.equalTo(1))
    }
}