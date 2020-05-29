package com.example.meteoapp.db

import androidx.room.TypeConverter
import com.example.meteoapp.db.model.CityType
import com.example.meteoapp.db.model.Month

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
        val valuesAndKeysList = value.subSequence(1, value.lastIndex - 1).replace("\\s".toRegex(), "").split(",")
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

private fun Double.round(decimals: Int): Double = (this * 100).toInt() / 100.0