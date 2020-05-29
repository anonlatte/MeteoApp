package com.example.meteoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.paging.toLiveData
import com.example.meteoapp.repository.Repository
import javax.inject.Inject

class HomeViewModel @Inject constructor(repository: Repository) : ViewModel() {
    val cities = repository.getCities().toLiveData(50)
    val weather = repository.getAllWeatherRows().toLiveData(50)
}