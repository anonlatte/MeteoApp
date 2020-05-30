package com.example.meteoapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.example.meteoapp.db.model.City

class CityViewHolder(private var parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_city, parent, false)
) {
    private val nameView = itemView.findViewById<TextView>(R.id.name)
    private val typeView = itemView.findViewById<TextView>(R.id.type)
    private val temperatureView = itemView.findViewById<TextView>(R.id.temperature)
    var city: City? = null

    fun bindTo(city: City?) {
        this.city = city
        nameView.text = city?.name
        typeView.text = parent.context.getString(city?.type?.typeToResource()!!)
        // TODO set temperature by settings of the temperature scales
        temperatureView.text = city.averageTemperatureBySeason.toString()
    }
}
