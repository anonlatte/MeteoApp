package com.example.meteoapp.ui.settings

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    val cities = repository.getCities().toLiveData(50)
    val weather = repository.getAllWeatherRows().toLiveData(50)
    lateinit var cityTypes: List<String>
    val temperatureUnits = MutableLiveData<Double>()

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

        if (temperatureUnit != 0) {
            for (key in weatherMap) {
                if (key.value != null) {
                    key.setValue(
                        temperatureStrategy.getTemperature(key.value!!)
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

}

