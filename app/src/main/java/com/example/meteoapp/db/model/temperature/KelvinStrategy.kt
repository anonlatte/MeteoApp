package com.example.meteoapp.db.model.temperature

class KelvinStrategy :
    TemperatureStrategy {
    override fun getValue(value: Double): Double = value - 273.15
}