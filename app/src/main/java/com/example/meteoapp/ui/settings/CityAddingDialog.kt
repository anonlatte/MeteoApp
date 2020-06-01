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
    }

    override fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?): MaterialAlertDialogBuilder {
        setView(null)
        return super.setOnDismissListener(onDismissListener)
    }

    override fun create(): AlertDialog {

        val monthsArray = arrayOf(
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

        cityNameEditText = dialogBinding.name
        cityTypeSpinner = dialogBinding.type

        monthsListAdapter = MonthsAdapter(monthsArray, viewModel.temperatureUnit)
        dialogBinding.monthsList.adapter = monthsListAdapter
        dialogBinding.monthsList.layoutManager = GridLayoutManager(context, 3)

        setTitle(R.string.title_dialog_add_city)
        setCancelable(true)

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

        setView(dialogBinding.root)

        return super.create()
    }

    override fun show(): AlertDialog {
        val alertDialog = super.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
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
                    val response = viewModel.addCity(
                        cityNameEditText.text.toString(),
                        cityTypeSpinner.selectedItem.toString()
                    )
                    if (response != null) {
                        val preparedWeatherMap = mutableMapOf<Month, Double?>()
                        monthsListAdapter.months.forEach { month ->
                            if (month.temperature != null) {
                                preparedWeatherMap[month] = month.temperature
                            }
                        }

                        viewModel.addWeatherToCity(
                            response,
                            preparedWeatherMap,
                            viewModel.temperatureUnit
                        )
                    }

                    val toastText =
                        if (response != null) "${cityNameEditText.text} - has been added!" else context.getString(
                            R.string.warning_unexpected_error
                        )

                    Toast.makeText(
                        context,
                        toastText,
                        Toast.LENGTH_LONG
                    ).show()

                    alertDialog.cancel()
                }
            }
        }


        return alertDialog
    }

}