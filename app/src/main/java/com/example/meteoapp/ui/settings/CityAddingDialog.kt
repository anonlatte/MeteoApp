package com.example.meteoapp.ui.settings

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.meteoapp.R
import com.example.meteoapp.databinding.DialogCityAddingBinding
import com.example.meteoapp.db.model.City
import com.example.meteoapp.db.model.Month
import com.example.meteoapp.db.model.Weather
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class CityAddingDialog(
    context: Context,
    private var dialogBinding: DialogCityAddingBinding,
    private var viewModel: SettingsViewModel,
    private var weather: Weather? = null,
    private var city: City? = null
) : MaterialAlertDialogBuilder(context) {

    private lateinit var monthsListAdapter: MonthsAdapter
    private lateinit var cityNameEditText: TextInputEditText
    private lateinit var cityTypeSpinner: Spinner

    companion object {
        /**
         * Regular expression for valid city name characters. Does not include any characters except EN and RU alphabet.
         */
        private const val PATTERN_CITY_NAME =
            "^[A-Z][a-z]+(?:[\\s-][a-zA-Z]+)*$|^[А-Я][а-я]+(?:[\\s-][а-яА-Я]+)*$"

        private val baseMonthsArray = arrayOf(
            Month.JANUARY,
            Month.FEBRUARY,
            Month.MARCH,
            Month.APRIL,
            Month.MAY,
            Month.JUNE,
            Month.JULY,
            Month.AUGUST,
            Month.SEPTEMBER,
            Month.OCTOBER,
            Month.NOVEMBER,
            Month.DECEMBER
        )
    }

    override fun create(): AlertDialog {
        cityNameEditText = dialogBinding.name
        cityTypeSpinner = dialogBinding.type

        monthsListAdapter = MonthsAdapter(baseMonthsArray, viewModel.temperatureUnit)
        dialogBinding.monthsList.adapter = monthsListAdapter
        dialogBinding.monthsList.layoutManager = GridLayoutManager(context, 3)

        setTitle(R.string.title_dialog_add_city)
        setCancelable(true)

        // Empty listener for overriding
        setPositiveButton(
            context.getString(R.string.action_add)
        ) { _, _ -> }

        setNegativeButton(context.getString(android.R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
        }

        cityNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enteredName = s?.toString()
                if (enteredName != null) {
                    if (!cityNameEditText.text!!.matches(PATTERN_CITY_NAME.toRegex())) {
                        cityNameEditText.error =
                            cityNameEditText.context.getString(R.string.warning_invalid_field)
                    } else {
                        cityNameEditText.error = null
                    }
                } else {
                    cityNameEditText.error =
                        cityNameEditText.context.getString(R.string.warning_empty_city_field)
                }
            }

        })

        if (city != null) {
            cityNameEditText.setText(city!!.name)
            cityTypeSpinner.setSelection(city!!.type.type + 1)
        }

        if (weather != null) {
            val retrievedWeather = weather!!.weatherMap
            val retrievedWeatherKeys = weather!!.weatherMap.keys
            baseMonthsArray.forEach {
                if (retrievedWeatherKeys.contains(it)) {
                    it.temperature = retrievedWeather[it]
                } else {
                    it.temperature = null
                }
            }
        } else {
            baseMonthsArray.forEach {
                it.temperature = null
            }
        }
        monthsListAdapter.notifyDataSetChanged()

        setView(dialogBinding.root)

        return super.create()
    }

    override fun setPositiveButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): MaterialAlertDialogBuilder {

        val positiveButtonClickListener = DialogInterface.OnClickListener { dialog, _ ->
            when {
                cityNameEditText.text.isNullOrEmpty() ->
                    cityNameEditText.error = context.getString(R.string.warning_empty_city_field)

                !cityNameEditText.text!!.matches(PATTERN_CITY_NAME.toRegex()) ->
                    cityNameEditText.error = context.getString(R.string.warning_invalid_field)

                cityTypeSpinner.selectedItem == "" -> {
                    val errorText = (cityTypeSpinner.selectedView as TextView)
                    errorText.error = ""
                    errorText.setTextColor(Color.RED)
                    errorText.text = context.getString(R.string.warning_empty_city_type_field)
                }
                else -> {
                    var cityResponse: Long? = null

                    val toastText: String

                    // If only weather data adding
                    if (city == null) {
                        cityResponse = viewModel.addCity(
                            cityNameEditText.text.toString(),
                            cityTypeSpinner.selectedItem.toString()
                        )
                    }

                    // collect filled values from adapter's array
                    val preparedWeatherMap = mutableMapOf<Month, Double?>()
                    monthsListAdapter.months.forEach { month ->
                        if (month.temperature != null) {
                            preparedWeatherMap[month] = month.temperature
                        }
                    }

                    // use retrieved id from response or from city instance
                    val weatherResponse = viewModel.addWeatherToCity(
                        city?.id ?: cityResponse!!,
                        preparedWeatherMap,
                        viewModel.temperatureUnit
                    )

                    // If we already have city instance just add weather
                    toastText = if (city != null) {
                        if (weatherResponse) "Weather has been updated!" else context.getString(
                            R.string.warning_unexpected_error
                        )
                    } else {
                        if (cityResponse != null) "${cityNameEditText.text} - has been added!" else context.getString(
                            R.string.warning_unexpected_error
                        )
                    }

                    Toast.makeText(
                        context,
                        toastText,
                        Toast.LENGTH_LONG
                    ).show()

                    dialog.cancel()
                }
            }

        }
        return super.setPositiveButton(text, positiveButtonClickListener)
    }
}