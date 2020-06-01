package com.example.meteoapp.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meteoapp.R
import com.example.meteoapp.databinding.DialogCityAddingBinding
import com.example.meteoapp.databinding.FragmentSettingsBinding
import com.example.meteoapp.db.model.CityType
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var dialogBinding: DialogCityAddingBinding

    @Inject
    lateinit var viewModel: SettingsViewModel
    private lateinit var adapter: SettingsCitiesAdapter
    private lateinit var sharedPreferences: SharedPreferences

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
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_city_adding,
            null,
            false
        )

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

            CityAddingDialog(requireContext(), dialogBinding, viewModel).create().show()
        }

        return binding.root
    }

    private fun initializeTemperatureSettings() {
        when (viewModel.temperatureUnit) {
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
        viewModel.temperatureUnit =
            sharedPreferences.getInt(getString(R.string.saved_temperature_unit), defaultValue)
        Log.d("Settings", "temperature unit is ${viewModel.temperatureUnit}.")
    }

    private fun changeTemperatureUnitsSettings(value: Int) {
        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()) {
            putInt(getString(R.string.saved_temperature_unit), value)
            commit()
        }
        viewModel.temperatureUnit = value
        Log.d("Settings", "temperature units changed to $value.")
    }

    private fun initializeAdapter() {
        adapter = SettingsCitiesAdapter(viewModel, binding)
        binding.settingsCitiesList.adapter = adapter
        binding.settingsCitiesList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeUI() {
        viewModel.cities.observe(viewLifecycleOwner, Observer { result ->
            binding.hasCities = !result.isNullOrEmpty()
            adapter.submitList(result)
        })
    }

}