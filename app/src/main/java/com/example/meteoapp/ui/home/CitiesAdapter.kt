package com.example.meteoapp.ui.home

import android.view.ViewGroup
import android.widget.CheckBox
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.meteoapp.R
import com.example.meteoapp.db.model.City

class CitiesAdapter(private var viewModel: HomeViewModel) :
    PagedListAdapter<City, CityViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CityViewHolder(parent)

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val addToFavoriteButton: CheckBox = holder.itemView.findViewById(R.id.addToFavoriteButton)
        val item = getItem(position)

        addToFavoriteButton.setButtonDrawable(R.drawable.sl_favourite_24dp)
        // TODO change to async checking
        val isCityFavorite = viewModel.isCityFavorite(item?.id!!)
        if (isCityFavorite) {
            addToFavoriteButton.isChecked = isCityFavorite
            addToFavoriteButton.setOnClickListener {
                if (!addToFavoriteButton.isChecked) {
                    viewModel.removeCityFromFavorite(item.id)
                } else {
                    viewModel.addCityToFavorite(item.id)
                }
            }
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