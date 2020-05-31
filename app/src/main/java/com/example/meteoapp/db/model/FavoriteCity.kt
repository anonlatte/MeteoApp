package com.example.meteoapp.db.model

import androidx.room.*

@Entity(
    indices = [Index("city_id", unique = true)],
    foreignKeys = [ForeignKey(
        entity = City::class,
        parentColumns = ["id"],
        childColumns = ["city_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    tableName = "favorite_city"
)
data class FavoriteCity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "city_id")
    var cityId: Long
)