package com.example.meteoapp.utils

import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.CityType
import com.example.meteoapp.db.model.Month
import com.example.meteoapp.db.model.Weather

val testCities = arrayListOf(
    City(1, "Архангельск", CityType.MEDIUM),
    City(2, "Ревда", CityType.SMALL),
    City(3, "Екатеринбург", CityType.BIG),
    City(4, "Санкт-Петербург", CityType.BIG),
    City(5, "Москва", CityType.BIG)
)

val testCity = City(6, "Воронеж", CityType.MEDIUM)

val testWeatherOfCities = arrayListOf(
    Weather(
        1,
        1,
        mutableMapOf(Month.JANUARY to -15.0, Month.DECEMBER to -10.5, Month.FEBRUARY to -30.1)
    ),
    Weather(2, 2, mutableMapOf(Month.JUNE to -24.88, Month.JULY to -15.61, Month.AUGUST to 37.79)),
    Weather(
        3,
        3,
        mutableMapOf(Month.OCTOBER to -9.46, Month.NOVEMBER to -2.22, Month.SEPTEMBER to -37.17)
    ),
    Weather(
        4,
        4,
        mutableMapOf(Month.FEBRUARY to -28.05, Month.MAY to -31.74, Month.AUGUST to -14.01)
    ),
    Weather(5, 5, mutableMapOf(Month.JULY to -5.95, Month.JUNE to -9.81, Month.JANUARY to -22.27))
)

val testWeather = Weather(
    6, 6, mutableMapOf(
        Month.FEBRUARY to -15.0, Month.OCTOBER to -10.5, Month.MAY to -30.1,
        Month.JANUARY to -15.0, Month.JULY to -10.5, Month.AUGUST to -30.1
    )
)

