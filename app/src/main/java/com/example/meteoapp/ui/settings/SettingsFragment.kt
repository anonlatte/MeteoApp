package com.example.meteoapp.ui.settings

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
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

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var viewModel: SettingsViewModel

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
        val adapter = SettingsCitiesAdapter()
        binding.settingsCitiesList.adapter = adapter
        binding.settingsCitiesList.layoutManager = LinearLayoutManager(requireContext())

        binding.addCityButton.setOnClickListener {
            val dialogBinding: DialogCityAddingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_city_adding,
                null,
                false
            )
            showCityAddingDialog(dialogBinding)
        }

        subscribeUI(adapter, binding)

        return binding.root
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


    private fun subscribeUI(adapter: SettingsCitiesAdapter, binding: FragmentSettingsBinding) {
        viewModel.cities.observe(viewLifecycleOwner, Observer { result ->
            binding.hasCities = !result.isNullOrEmpty()
            adapter.submitList(result)
        })
    }

}