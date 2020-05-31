package com.example.meteoapp.db.model.temperature

interface TemperatureStrategy {

    fun toCelsius(value: Double): Double

    fun fromCelsius(value: Double): Double

}