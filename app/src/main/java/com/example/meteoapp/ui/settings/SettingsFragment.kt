package com.example.meteoapp.ui.settings

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meteoapp.R
import com.example.meteoapp.databinding.DialogCityAddingBinding
import com.example.meteoapp.databinding.FragmentSettingsBinding
import com.example.meteoapp.db.model.CityType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class SettingsFragment : Fragment() {

    private var temperatureUnit: Int = 0
    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var viewModel: SettingsViewModel
    private lateinit var adapter: SettingsCitiesAdapter
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        /**
         * Regular expression for valid city name characters. Does not include any characters except EN and RU alphabet.
         */
        private const val PATTERN_CITY_NAME = "^[A-Z][a-z]+|^[А-Я][а-я]+"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel.cityTypes = listOf(
            getString(CityType.SMALL.typeToResource()),
            getString(CityType.MEDIUM.typeToResource()),
            getString(CityType.BIG.typeToResource())
        )

        initializeSettings()
        initializeTemperatureSettings()
        initializeAdapter()
        subscribeUI()

        binding.addCityButton.setOnClickListener {
            val dialogBinding: DialogCityAddingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_city_adding,
                null,
                false
            )
            showCityAddingDialog(dialogBinding)
        }

        return binding.root
    }

    private fun initializeTemperatureSettings() {
        when (temperatureUnit) {
            0 -> {
                binding.temperatureUnits.check(R.id.celsiusButton)
            }
            1 -> {
                binding.temperatureUnits.check(R.id.fahrenheitButton)
            }
            else -> {
                binding.temperatureUnits.check(R.id.kelvinButton)
            }
        }

        binding.temperatureUnits.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (group.checkedButtonId) {
                    R.id.celsiusButton -> changeTemperatureUnitsSettings(0)
                    R.id.fahrenheitButton -> changeTemperatureUnitsSettings(1)
                    R.id.kelvinButton -> changeTemperatureUnitsSettings(2)
                    else -> changeTemperatureUnitsSettings(0)
                }
            }
        }

    }

    private fun initializeSettings() {
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultValue = resources.getInteger(R.integer.saved_temperature_units_default_key)
        temperatureUnit =
            sharedPreferences.getInt(getString(R.string.saved_temperature_unit), defaultValue)
        Log.d("Settings", "temperature unit is $temperatureUnit.")
    }

    private fun changeTemperatureUnitsSettings(value: Int) {
        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()) {
            putInt(getString(R.string.saved_temperature_unit), value)
            commit()
        }
        Log.d("Settings", "temperature units changed to $value.")
    }

    private fun initializeAdapter() {
        adapter = SettingsCitiesAdapter()
        binding.settingsCitiesList.adapter = adapter
        binding.settingsCitiesList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showCityAddingDialog(dialogBinding: DialogCityAddingBinding) {
        val dialog: AlertDialog =
            MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.title_dialog_add_city)
                .setCancelable(true)
                .setPositiveButton(
                    getString(R.string.action_add)
                ) { _, _ -> }
                .setNegativeButton(getString(android.R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }.create()

        dialog.setView(dialogBinding.root)
        dialog.show()
        val cityNameEditText = dialogBinding.name
        val cityTypeSpinner = dialogBinding.type

        // Overriding positive button for a validation of the fields.
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            when {
                cityNameEditText.text.isNullOrEmpty() ->
                    cityNameEditText.error = getString(R.string.warning_empty_city_field)

                !cityNameEditText.text!!.matches(PATTERN_CITY_NAME.toRegex()) ->
                    cityNameEditText.error = getString(R.string.warning_invalid_field)

                cityTypeSpinner.selectedItem == "" -> {
                    val errorText = (cityTypeSpinner.selectedView as TextView)
                    errorText.error = ""
                    errorText.setTextColor(Color.RED)
                    errorText.text = getString(R.string.warning_empty_city_type_field)
                }
                else -> {
                    val response = (viewModel.addCity(
                        cityNameEditText.text.toString(),
                        cityTypeSpinner.selectedItem.toString()
                    ))

                    val snackbarText =
                        if (response) "${cityNameEditText.text} - has been added!" else getString(R.string.warning_unexpected_error)

                    Toast.makeText(
                        requireContext(),
                        snackbarText,
                        Toast.LENGTH_LONG
                    ).show()

                    dialog.cancel()
                }
            }
        }
    }


    private fun subscribeUI() {
        viewModel.cities.observe(viewLifecycleOwner, Observer { result ->
            binding.hasCities = !result.isNullOrEmpty()
            adapter.submitList(result)
        })
    }

}