package com.example.meteoapp.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.meteoapp.R
import com.example.meteoapp.databinding.DialogCityAddingBinding
import com.example.meteoapp.databinding.FragmentSettingsBinding
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.Weather
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsCitiesAdapter(
    var viewModel: SettingsViewModel,
    private var cityAddingBinding: FragmentSettingsBinding
) : PagedListAdapter<City, SettingsCityViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SettingsCityViewHolder(parent)

    override fun onBindViewHolder(holder: SettingsCityViewHolder, position: Int) {

        val item = getItem(position)
        val context = cityAddingBinding.root.context

        holder.itemView.setOnClickListener {
            val retrievedWeather: Weather?
            if (item != null) {
                retrievedWeather = viewModel.getWeatherByCityId(item.id)

                val dialogBinding: DialogCityAddingBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.dialog_city_adding,
                    null,
                    false
                )

                if (retrievedWeather != null) {
                    CityAddingDialog(
                        context,
                        dialogBinding,
                        viewModel,
                        retrievedWeather,
                        item
                    ).show()
                } else {
                    CityAddingDialog(
                        context,
                        dialogBinding,
                        viewModel,
                        city = item
                    ).show()
                }
            }

        }

        holder.itemView.setOnLongClickListener {
            val removeDialog = MaterialAlertDialogBuilder(context)
                .setMessage(context.getString(R.string.message_remove_city))
                .setPositiveButton(
                    context.getString(R.string.action_remove_city)
                ) { dialog, _ ->
                    val response = viewModel.deleteCity(item!!)
                    val toastText = if (response > 0) {
                        context.getString(R.string.response_delete_city)
                    } else {
                        context.getString(R.string.warning_unexpected_error)
                    }
                    Toast.makeText(
                        context,
                        toastText,
                        Toast.LENGTH_LONG
                    ).show()

                    dialog.dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
            removeDialog.show()
            return@setOnLongClickListener true
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