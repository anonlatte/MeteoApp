package com.example.meteoapp.db.model.temperature

interface TemperatureStrategy {
    fun getValue(value: Double): Double
}