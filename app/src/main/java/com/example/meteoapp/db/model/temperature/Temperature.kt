package com.example.meteoapp.db.model.temperature

class Temperature(private val temperatureStrategy: TemperatureStrategy) {

    fun toCelsius(value: Double): Double = temperatureStrategy.toCelsius(value)

    fun fromCelsius(value: Double): Double = temperatureStrategy.fromCelsius(value)

    val minTemperature = -90
    val maxTemperature = 60

}