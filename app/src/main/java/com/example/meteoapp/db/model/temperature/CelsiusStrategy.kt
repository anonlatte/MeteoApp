package com.example.meteoapp.db.model.temperature

class CelsiusStrategy : TemperatureStrategy {

    override fun toCelsius(value: Double) = value

    override fun fromCelsius(value: Double) = value

}