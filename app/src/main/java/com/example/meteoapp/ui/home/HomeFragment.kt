package com.example.meteoapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meteoapp.databinding.FragmentHomeBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModel: HomeViewModel
    private lateinit var adapter: CitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initializeAdapter()
        subscribeUI()
        return binding.root
    }

    private fun initializeAdapter() {
        adapter = CitiesAdapter(viewModel)
        binding.citiesList.adapter = adapter
        binding.citiesList.layoutManager = LinearLayoutManager(requireContext())
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
    }
}