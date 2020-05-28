package com.example.meteoapp.db.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("name", unique = true)])
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var type: CityType
)

enum class CityType(val type: Int) {
    SMALL(0), MEDIUM(1), BIG(2)
}
