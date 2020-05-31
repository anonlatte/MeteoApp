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
    val weatherId: Long = 0,
    @ColumnInfo(name = "city_id")
    val cityId: Long,

    @ColumnInfo(name = "weather_map")
    var weatherMap: MutableMap<Month, Double?>
)

