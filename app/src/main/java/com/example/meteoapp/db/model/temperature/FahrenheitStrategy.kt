package com.example.meteoapp.db.model.temperature

class FahrenheitStrategy : TemperatureStrategy {

    override fun toCelsius(value: Double): Double = (value - 32) * 5 / 9

    override fun fromCelsius(value: Double): Double = (value * 9 / 5) + 32
}

