package com.example.meteoapp.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.meteoapp.R
import com.example.meteoapp.databinding.DialogCityAddingBinding
import com.example.meteoapp.databinding.FragmentSettingsBinding
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.Weather

class SettingsCitiesAdapter(
    var viewModel: SettingsViewModel,
    private var cityAddingBinding: FragmentSettingsBinding
) : PagedListAdapter<City, SettingsCityViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SettingsCityViewHolder(parent)

    override fun onBindViewHolder(holder: SettingsCityViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            val retrievedWeather: Weather?
            if (item != null) {
                retrievedWeather = viewModel.getWeatherByCityId(item.id)

                val dialogBinding: DialogCityAddingBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(cityAddingBinding.root.context),
                    R.layout.dialog_city_adding,
                    null,
                    false
                )

                if (retrievedWeather != null) {
                    CityAddingDialog(
                        cityAddingBinding.root.context,
                        dialogBinding,
                        viewModel,
                        retrievedWeather,
                        item
                    ).show()
                } else {
                    CityAddingDialog(
                        cityAddingBinding.root.context,
                        dialogBinding,
                        viewModel,
                        city = item
                    ).show()
                }
            }

        }
        holder.bindTo(item)
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