package com.example.meteoapp.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.toLiveData
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.CityType
import com.example.meteoapp.db.model.Month
import com.example.meteoapp.db.model.Weather
import com.example.meteoapp.db.model.temperature.CelsiusStrategy
import com.example.meteoapp.db.model.temperature.FahrenheitStrategy
import com.example.meteoapp.db.model.temperature.KelvinStrategy
import com.example.meteoapp.db.model.temperature.Temperature
import com.example.meteoapp.repository.Repository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var temperatureUnit: Int = 0
    val cities = repository.getCities().toLiveData(50)
    val weather = repository.getAllWeatherRows().toLiveData(50)
    lateinit var cityTypes: List<String>

    fun addCity(cityName: String, cityType: String): Long? {
        var responseHandler: Long? = null
        runBlocking {
            try {
                val newCity = City(name = cityName, type = cityType.stringToCityType())
                responseHandler = repository.createCity(newCity)
                Log.i("Room", "$newCity has been added to table.")
            } catch (e: Exception) {
                Log.w("Room", e.message.toString())
            }
        }
        return responseHandler
    }

    fun deleteCity(city: City): Int {
        var responseHandler = 0
        runBlocking {
            try {
                responseHandler = repository.deleteCity(city)
                Log.i("Room", "$city has been deleted.")
            } catch (e: Exception) {
                Log.w("Room", e.message.toString())
            }
        }
        return responseHandler
    }

    fun addWeatherToCity(
        cityId: Long,
        weatherMap: MutableMap<Month, Double?>,
        temperatureUnit: Int
    ): Boolean {
        var responseHandler = true

        val temperatureStrategy: Temperature = when (temperatureUnit) {
            1 -> Temperature(FahrenheitStrategy())
            2 -> Temperature(KelvinStrategy())
            else -> Temperature(CelsiusStrategy())
        }

        // If is not default (celsius)
        if (temperatureUnit != 0) {
            for (key in weatherMap) {
                if (key.value != null) {
                    key.setValue(
                        temperatureStrategy.toCelsius(key.value!!)
                    )
                }
            }
        }

        viewModelScope.launch {
            try {
                val newWeather = Weather(cityId = cityId, weatherMap = weatherMap)
                repository.createWeather(newWeather)
                Log.i("Room", "$newWeather has been added to table.")
            } catch (e: Exception) {
                responseHandler = false
                Log.w("Room", e.message.toString())
            }
        }
        return responseHandler
    }

    private fun String.stringToCityType() = when (this) {
        "Small" -> CityType.SMALL
        "Medium" -> CityType.MEDIUM
        "Big" -> CityType.BIG
        else -> CityType.UNKNOWN
    }

    // FIXME DRY
    fun getWeatherByCityId(cityId: Long): Weather? {
        var response: Weather? = null
        runBlocking {
            response = repository.getWeatherByCity(cityId)
            Log.i("Room", "got $response")
        }
        return response
    }

}

