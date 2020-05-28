package com.example.meteoapp.db.model

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = City::class,
        parentColumns = ["id"],
        childColumns = ["city_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["city_id"], unique = true)]
)

data class Weather(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "weather_id")
    val weatherId: Int,
    @ColumnInfo(name = "city_id")
    val cityId: Int,
    @ColumnInfo(name = "weather_map")
    var weatherMap: MutableMap<Month, Double>
)

enum class Month(val monthNumber: Int) {
    DECEMBER(11), JANUARY(0), FEBRUARY(1),
    MARCH(2), APRIL(3), MAY(4),
    JUNE(5), JULY(6), AUGUST(7),
    SEPTEMBER(8), OCTOBER(9), NOVEMBER(10),
    UNKNOWN(-1);
}
