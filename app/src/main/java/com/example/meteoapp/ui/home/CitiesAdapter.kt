package com.example.meteoapp.ui.home

import android.view.ViewGroup
import android.widget.CheckBox
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.meteoapp.R
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.Month
import com.example.meteoapp.db.model.Season
import com.example.meteoapp.db.model.temperature.CelsiusStrategy
import com.example.meteoapp.db.model.temperature.FahrenheitStrategy
import com.example.meteoapp.db.model.temperature.KelvinStrategy
import com.example.meteoapp.db.model.temperature.Temperature
import com.example.meteoapp.db.round

class CitiesAdapter(private var viewModel: HomeViewModel) :
    PagedListAdapter<City, CityViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CityViewHolder(parent)

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val addToFavoriteButton: CheckBox = holder.itemView.findViewById(R.id.addToFavoriteButton)
        val item = getItem(position)

        addToFavoriteButton.setButtonDrawable(R.drawable.sl_favourite_24dp)
        // TODO change to async checking
        val isCityFavorite = viewModel.isCityFavorite(item?.id!!)
        if (isCityFavorite) {
            addToFavoriteButton.isChecked = isCityFavorite
        }
        addToFavoriteButton.setOnClickListener {
            if (!addToFavoriteButton.isChecked) {
                viewModel.removeCityFromFavorite(item.id)
            } else {
                viewModel.addCityToFavorite(item.id)
            }
        }

        val weather = viewModel.getWeatherByCityId(item.id)
        item.selectedSeason = viewModel.selectedSeason.value!!
        if (weather != null) {
            item.averageTemperatureBySeason = convertTemperature(
                when (item.selectedSeason) {
                    Season.WINTER -> {
                        middleAverage(
                            weather.weatherMap[Month.DECEMBER],
                            weather.weatherMap[Month.JANUARY],
                            weather.weatherMap[Month.FEBRUARY]
                        )
                    }
                    Season.SPRING -> {
                        middleAverage(
                            weather.weatherMap[Month.MARCH],
                            weather.weatherMap[Month.APRIL],
                            weather.weatherMap[Month.MAY]
                        )

                    }
                    Season.SUMMER -> {
                        middleAverage(
                            weather.weatherMap[Month.JUNE],
                            weather.weatherMap[Month.JULY],
                            weather.weatherMap[Month.AUGUST]
                        )

                    }
                    Season.AUTUMN -> {
                        middleAverage(
                            weather.weatherMap[Month.SEPTEMBER],
                            weather.weatherMap[Month.OCTOBER],
                            weather.weatherMap[Month.NOVEMBER]
                        )

                    }
                }, viewModel.temperatureUnits.value!!
            ).round(2)
        }

        holder.bindTo(item, viewModel.temperatureUnits.value!!)
    }

    private fun convertTemperature(value: Double, temperatureUnit: Int): Double {
        val temperatureStrategy = when (temperatureUnit) {
            1 -> {
                Temperature(FahrenheitStrategy())
            }
            2 -> {
                Temperature(KelvinStrategy())
            }
            else -> {
                Temperature(CelsiusStrategy())
            }
        }
        return temperatureStrategy.fromCelsius(value)
    }

    private fun middleAverage(vararg items: Double?): Double {
        var sum = 0.0
        var count = 0
        for (element in items) {
            if (element != null) {
                sum += element
                ++count
            }
        }
        return if (count == 0) 0.0 else (sum / count)
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<City>() {

            override fun areItemsTheSame(oldItem: City, newItem: City): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: City, newItem: City): Boolean =
                oldItem == newItem
        }
    }
}