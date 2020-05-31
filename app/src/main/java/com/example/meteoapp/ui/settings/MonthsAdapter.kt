package com.example.meteoapp.ui.settings

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.example.meteoapp.db.model.Month
import com.example.meteoapp.db.model.temperature.CelsiusStrategy
import com.example.meteoapp.db.model.temperature.FahrenheitStrategy
import com.example.meteoapp.db.model.temperature.KelvinStrategy
import com.example.meteoapp.db.model.temperature.Temperature

class MonthsAdapter(var months: Array<Month>, var temperatureUnit: Int) :
    RecyclerView.Adapter<MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MonthViewHolder(parent)

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthTemperatureEditText = holder.itemView.findViewById<EditText>(R.id.monthTemperature)
        monthTemperatureEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enteredNumber = s?.toString()?.toDoubleOrNull()
                if (enteredNumber != null) {
                    if (!isEnteredTemperatureValid(enteredNumber)) {
                        monthTemperatureEditText.error =
                            monthTemperatureEditText.context.getString(R.string.warning_invalid_field)
                    } else {
                        monthTemperatureEditText.error = null
                        months[position].temperature = enteredNumber
                    }
                }
            }

        })

        holder.bindTo(months[position])
    }

    fun isEnteredTemperatureValid(value: Double?): Boolean {
        val temperatureStrategy: Temperature
        val convertedTemperature: Double
        return if (value != null) {
            temperatureStrategy = when (temperatureUnit) {
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

            convertedTemperature = temperatureStrategy.toCelsius(value)

            convertedTemperature >= temperatureStrategy.minTemperature &&
                    convertedTemperature <= temperatureStrategy.maxTemperature
        } else {
            false
        }
    }

    override fun getItemCount(): Int = months.size
}