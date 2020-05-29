package com.example.meteoapp.db

import androidx.room.TypeConverter
import com.example.meteoapp.db.model.CityType
import com.example.meteoapp.db.model.Month
import kotlin.math.abs
import kotlin.math.round

class Converters {

    @TypeConverter
    fun cityTypeToInt(cityType: CityType): Int = cityType.type

    @TypeConverter
    fun intToCityType(value: Int) = when (value) {
        0 -> CityType.SMALL
        1 -> CityType.MEDIUM
        2 -> CityType.BIG
        else -> CityType.SMALL
    }


    @TypeConverter
    fun weatherMapToString(weatherMap: MutableMap<Month, Double>): String {
        val convertedWeatherMap = mutableMapOf<Int, Double>()
        for (weather in weatherMap) {
            convertedWeatherMap[weather.key.getValue()] = weather.value.round(2)
        }
        return convertedWeatherMap.toString()
    }

    @TypeConverter
    fun stringToWeatherMap(value: String): MutableMap<Month, Double> {
        val valuesAndKeysList =
            value.subSequence(1, value.lastIndex - 1).replace("\\s".toRegex(), "").split(",")
        val weatherMap = mutableMapOf<Month, Double>()
        valuesAndKeysList.forEach {
            val monthAndWeather = it.split("=")
            weatherMap[intToMonth(monthAndWeather[0].toInt())] =
                monthAndWeather[1].toDouble()
        }
        return weatherMap
    }

    @TypeConverter
    fun monthToInt(month: Month) = month.getValue()

    @TypeConverter
    fun intToMonth(value: Int) = when (value) {
        1 -> Month.JANUARY
        2 -> Month.FEBRUARY
        3 -> Month.MARCH
        4 -> Month.APRIL
        5 -> Month.MAY
        6 -> Month.JUNE
        7 -> Month.JULY
        8 -> Month.AUGUST
        9 -> Month.SEPTEMBER
        10 -> Month.OCTOBER
        11 -> Month.NOVEMBER
        12 -> Month.DECEMBER
        else -> Month.UNKNOWN
    }

}

/**
 * Rounds the double number to the given value of [decimals] by module or rounds towards the closest integer with ties rounded towards even integer.
 *
 * Special cases:
 *   - `round(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer.
 */
private fun Double.round(decimals: Int = 0): Double {
    val absDecimals = abs(decimals)
    return if (absDecimals > 0) {
        var multiplier: Double = 10.0
        for (i in 1 until absDecimals) {
            multiplier *= 10
        }
        (this * multiplier).toInt() / multiplier
    } else {
        round(this)
    }
}