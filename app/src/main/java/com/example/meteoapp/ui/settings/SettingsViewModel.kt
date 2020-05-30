package com.example.meteoapp.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.toLiveData
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.CityType
import com.example.meteoapp.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val cities = repository.getCities().toLiveData(50)

    //    private var _citiesMutable: MutableLiveData<PagedList<City> = MutableLiveData(cities)
    val weather = repository.getAllWeatherRows().toLiveData(50)
    lateinit var cityTypes: List<String>

    fun addCity(cityName: String, cityType: String): Boolean {
        var responseHandler = true
        viewModelScope.launch {
            try {
                val newCity = City(name = cityName, type = cityType.stringToCityType())
                repository.createCity(newCity)
//                cities.value!!.add(newCity)
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

