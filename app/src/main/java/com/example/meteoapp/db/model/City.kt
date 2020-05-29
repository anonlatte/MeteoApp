package com.example.meteoapp.db.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.meteoapp.R

@Entity(indices = [Index("name", unique = true)])
data class City(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String,
    var type: CityType
) {
    var averageTemperatureBySeason: Double = 0.0
}

enum class CityType(val type: Int) {
    SMALL(0), MEDIUM(1), BIG(2);

    fun typeToResource(): Int {
        return when (type) {
            0 -> R.string.city_type_small
            1 -> R.string.city_type_medium
            2 -> R.string.city_type_big
            else -> R.string.city_type_unknown
        }
    }
}
