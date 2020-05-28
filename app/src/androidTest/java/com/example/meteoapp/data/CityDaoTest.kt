package com.example.meteoapp.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.meteoapp.db.AppDatabase
import com.example.meteoapp.db.dao.CityDao
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.CityType
import com.example.meteoapp.utils.getValue
import com.example.meteoapp.utils.testCities
import com.example.meteoapp.utils.testCity
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CityDaoTest {

    private lateinit var cityDao: CityDao
    private lateinit var db: AppDatabase
    private var testCityId: Long = -1

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        cityDao = db.cityDao()
        cityDao.insertAll(testCities)
        testCityId = cityDao.createCity(testCity)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetCities() = runBlocking {
        val city = City(7, "Рязань", CityType.SMALL)
        cityDao.createCity(city)
        getValue(cityDao.getCities()).forEach {
            print(it.toString())
        }
        assertThat(getValue(cityDao.getCities()).size, equalTo(7))
    }

    @Test
    fun testGetCityByName() = runBlocking {
        val city = cityDao.getCityByName(testCity.name)
        assertThat(getValue(city).id.toLong(), equalTo(testCityId))
    }

    @Test
    fun testUpdateCity() = runBlocking {
        testCity.name = "Новоуральск"
        assertThat(cityDao.updateCity(testCity), equalTo(1))
    }

    @Test
    fun testDeleteCity() = runBlocking {
        assertThat(cityDao.deleteCity(testCity), equalTo(1))
    }
}


