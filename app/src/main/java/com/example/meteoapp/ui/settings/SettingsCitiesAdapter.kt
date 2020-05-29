package com.example.meteoapp.ui.settings

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.meteoapp.db.model.City

class SettingsCitiesAdapter : PagedListAdapter<City, SettingsCityViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SettingsCityViewHolder(parent)

    override fun onBindViewHolder(holder: SettingsCityViewHolder, position: Int) {
        holder.bindTo(getItem(position))
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