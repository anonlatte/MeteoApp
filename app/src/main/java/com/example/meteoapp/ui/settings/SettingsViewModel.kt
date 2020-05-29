package com.example.meteoapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.paging.toLiveData
import com.example.meteoapp.repository.Repository
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val cities = repository.getCities().toLiveData(50)
    val weather = repository.getAllWeatherRows().toLiveData(50)
    lateinit var cityTypes: List<String>
}