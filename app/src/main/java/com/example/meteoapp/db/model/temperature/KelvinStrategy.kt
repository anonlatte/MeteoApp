package com.example.meteoapp.db.model.temperature

class KelvinStrategy : TemperatureStrategy {

    override fun toCelsius(value: Double): Double = value - 273.15

    override fun fromCelsius(value: Double): Double = value + 273.15

}