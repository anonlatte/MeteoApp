package com.example.meteoapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.toLiveData
import com.example.meteoapp.db.model.FavoriteCity
import com.example.meteoapp.repository.Repository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class HomeViewModel @Inject constructor(private var repository: Repository) : ViewModel() {
    val cities = repository.getCities().toLiveData(50)
    val weather = repository.getAllWeatherRows().toLiveData(50)

    fun addCityToFavorite(cityId: Long) = viewModelScope.launch {
        repository.addToFavorites(FavoriteCity(cityId = cityId))
        Log.i("Room", "City with id=$cityId has been added to favorites")
    }

    fun isCityFavorite(cityId: Long): Boolean {
        var response = false
        // TODO change to async checking
        runBlocking {
            val city = repository.getFavoriteCityById(cityId)
            response = city != null
        }
        Log.i("Room", "Is city with id=$cityId favorite - $response")
        return response
    }

    fun removeCityFromFavorite(cityId: Long) = viewModelScope.launch {
        repository.removeFromFavorites(cityId)
        Log.i("Room", "City with id=$cityId has been removed from favorites")
    }
}