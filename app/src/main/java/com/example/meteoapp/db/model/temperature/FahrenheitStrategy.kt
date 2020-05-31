package com.example.meteoapp.db.model.temperature

class FahrenheitStrategy :
    TemperatureStrategy {
    override fun getValue(value: Double): Double = (value - 32) * 5 / 9
}

