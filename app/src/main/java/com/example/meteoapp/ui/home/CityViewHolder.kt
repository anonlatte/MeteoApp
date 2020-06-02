package com.example.meteoapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.Season

class CityViewHolder(private var parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_city, parent, false)
) {
    private val nameTextView = itemView.findViewById<TextView>(R.id.name)
    private val typeTextView = itemView.findViewById<TextView>(R.id.type)
    private val temperatureTextView = itemView.findViewById<TextView>(R.id.temperature)
    private val seasonTextView = itemView.findViewById<TextView>(R.id.season)
    var city: City? = null
    private var textTemperatureUnit = ""
    private var itemViewContext: Context = itemView.context

    fun bindTo(city: City?, temperatureUnit: Int) {
        this.city = city
        nameTextView.text = city?.name
        typeTextView.text = parent.context.getString(city?.type?.typeToResource()!!)

        if (city.averageTemperatureBySeason != 0.0) {
            textTemperatureUnit = when (temperatureUnit) {
                1 -> {
                    itemViewContext.getString(
                        R.string.parsed_units_temperature_fahrenheit,
                        city.averageTemperatureBySeason.toString()
                    )
                }
                2 -> {
                    itemViewContext.getString(
                        R.string.parsed_units_temperature_kelvin,
                        city.averageTemperatureBySeason.toString()
                    )
                }
                else -> {
                    itemViewContext.getString(
                        R.string.parsed_units_temperature_celsius,
                        city.averageTemperatureBySeason.toString()
                    )
                }
            }
        }

        temperatureTextView.text = textTemperatureUnit

        seasonTextView.text = when (city.selectedSeason) {
            Season.WINTER -> {
                itemViewContext.getString(R.string.weather_season_winter)
            }
            Season.SPRING -> {
                itemViewContext.getString(R.string.weather_season_spring)
            }
            Season.SUMMER -> {
                itemViewContext.getString(R.string.weather_season_summer)
            }
            Season.AUTUMN -> {
                itemViewContext.getString(R.string.weather_season_autumn)
            }
        }
    }
}
