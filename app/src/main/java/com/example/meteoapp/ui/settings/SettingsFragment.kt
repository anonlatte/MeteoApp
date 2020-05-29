package com.example.meteoapp.ui.settings

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.meteoapp.R
import com.example.meteoapp.databinding.DialogCityAddingBinding
import com.example.meteoapp.databinding.FragmentSettingsBinding
import com.example.meteoapp.db.model.CityType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var viewModel: SettingsViewModel

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

        binding.addCityButton.setOnClickListener {
            val dialogBinding: DialogCityAddingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_city_adding,
                null,
                false
            )
            val dialog =
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.title_dialog_add_city)
                    .setCancelable(true)
                    .setPositiveButton(
                        getString(R.string.action_add)
                    ) { dialog, _ ->
                        Snackbar.make(
                            requireView(),
                            "${dialogBinding.name.text}:${dialogBinding.type.selectedItem} - has been added!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton(getString(android.R.string.cancel)) { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.cancel()
                    }
            dialog.setView(dialogBinding.root)
            dialog.show()
        }
        subscribeUI(adapter, binding)

        return binding.root
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun subscribeUI(adapter: SettingsCitiesAdapter, binding: FragmentSettingsBinding) {
        viewModel.cities.observe(viewLifecycleOwner, Observer { result ->
            binding.hasCities = !result.isNullOrEmpty()
            adapter.submitList(result)
        })
    }

}