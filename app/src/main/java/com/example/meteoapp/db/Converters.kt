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
    fun weatherMapToString(weatherMap: MutableMap<Month, Double>) = weatherMap.toString()

    @TypeConverter
    fun stringToWeatherMap(value: String): MutableMap<Month, Double> {
        val valuesAndKeysList = value.subSequence(1, value.lastIndex - 1).split(",")
        val weatherMap = mutableMapOf<Month, Double>()
        valuesAndKeysList.forEach {
            val monthAndWeather = it.split("=")
            val test = Month.DECEMBER
            weatherMap[intToMonth(stringMonthToInt(monthAndWeather[0]))] =
                monthAndWeather[1].toDouble()
        }
        return weatherMap
    }

    @TypeConverter
    fun monthToInt(month: Month) = month.monthNumber

    @TypeConverter
    fun intToMonth(value: Int) = when (value) {
        0 -> Month.JANUARY
        1 -> Month.FEBRUARY
        2 -> Month.MARCH
        3 -> Month.APRIL
        4 -> Month.MAY
        5 -> Month.JUNE
        6 -> Month.JULY
        7 -> Month.AUGUST
        8 -> Month.SEPTEMBER
        9 -> Month.OCTOBER
        10 -> Month.NOVEMBER
        11 -> Month.DECEMBER
        else -> Month.UNKNOWN
    }

    private fun stringMonthToInt(value: String) =
        when (value) {
            "JANUARY" -> 0
            "FEBRUARY" -> 1
            "MARCH" -> 2
            "APRIL" -> 3
            "MAY" -> 4
            "JUNE" -> 5
            "JULY" -> 6
            "AUGUST" -> 7
            "SEPTEMBER" -> 8
            "OCTOBER" -> 9
            "NOVEMBER" -> 10
            "DECEMBER" -> 11
            else -> -1
        }
}