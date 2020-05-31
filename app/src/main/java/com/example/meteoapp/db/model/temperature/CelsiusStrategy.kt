package com.example.meteoapp.db.model.temperature

class CelsiusStrategy :
    TemperatureStrategy {
    override fun getValue(value: Double) = value
}