package com.example.meteoapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meteoapp.R
import com.example.meteoapp.databinding.FragmentHomeBinding
import com.example.meteoapp.db.model.Season
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModel: HomeViewModel
    private lateinit var adapter: CitiesAdapter

    private var isFABMenuOpen = false
    private var fabMenuButtonSize: Float = 0F
    private var fabSubMenuButtonSize: Float = 0F
    private var fabMenuGutter: Float = 0F

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initializeAdapter()
        initializeSettings()
        subscribeUI()
        fabSubMenuButtonSize = resources.getDimension(R.dimen.fab_sub_menu_size)
        fabMenuButtonSize = resources.getDimension(R.dimen.fab_main_button_size)
        fabMenuGutter = resources.getDimension(R.dimen.fab_menu_gutter)
        binding.seasonChangeButton.setOnClickListener {
            if (isFABMenuOpen) {
                closeFABMenu()
            } else {
                showFABMenu()
            }
        }

        binding.winterButton.setOnClickListener {
            viewModel.selectedSeason.value = Season.WINTER
        }
        binding.springButton.setOnClickListener {
            viewModel.selectedSeason.value = Season.SPRING
        }
        binding.summerButton.setOnClickListener {
            viewModel.selectedSeason.value = Season.SUMMER
        }
        binding.autumnButton.setOnClickListener {
            viewModel.selectedSeason.value = Season.AUTUMN
        }

        return binding.root
    }

    private fun showFABMenu() {
        isFABMenuOpen = true

        binding.winterButton.animate()
            .translationX((fabMenuButtonSize * 1.25F) * -cos(56.25F))
            .translationY((fabMenuButtonSize * 1.25F) * -sin(56.25F))
        binding.springButton.animate()
            .translationX((fabMenuButtonSize * 1.25F) * cos(22.5F))
            .translationY((fabMenuButtonSize * 1.25F) * sin(22.5F))

        binding.summerButton.animate()
            .translationX(-(fabMenuButtonSize * 1.25F) * cos(11.25F))
            .translationY((fabMenuButtonSize * 1.25F) * sin(11.25F))

        binding.autumnButton.animate()
            .translationX((fabMenuButtonSize * 1.25F) * -cos(90F))
            .translationY((fabMenuButtonSize * 1.25F) * -sin(90F))
    }

    private fun closeFABMenu() {
        isFABMenuOpen = false
        binding.winterButton.animate().translationX(0F).translationY(0F)
        binding.springButton.animate().translationX(0F).translationY(0F)
        binding.summerButton.animate().translationX(0F).translationY(0F)
        binding.autumnButton.animate().translationX(0F).translationY(0F)
    }

    private fun initializeAdapter() {
        adapter = CitiesAdapter(viewModel)
        binding.citiesList.adapter = adapter
        binding.citiesList.layoutManager = LinearLayoutManager(requireContext())
    }

    // FIXME DRY
    private fun initializeSettings() {
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultValue = resources.getInteger(R.integer.saved_temperature_units_default_key)
        viewModel.temperatureUnits.value =
            sharedPreferences.getInt(getString(R.string.saved_temperature_unit), defaultValue)
        Log.d("Settings", "temperature unit is ${viewModel.temperatureUnits.value}.")
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun subscribeUI() {
        viewModel.cities.observe(viewLifecycleOwner, Observer { result ->
            binding.hasCities = !result.isNullOrEmpty()
            adapter.submitList(result)
        })

        viewModel.selectedSeason.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })
    }
}