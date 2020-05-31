package com.example.meteoapp.db.model.temperature

class Temperature(private val temperatureStrategy: TemperatureStrategy) {
    fun getTemperature(value: Double): Double =
        temperatureStrategy.getValue(value)

    val minTemperature = -90
    val maxTemperature = 60
}