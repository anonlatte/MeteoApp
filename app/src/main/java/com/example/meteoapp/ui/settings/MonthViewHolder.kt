package com.example.meteoapp.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.example.meteoapp.db.model.Month

class MonthViewHolder(private var parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_month, parent, false)
) {
    private val monthName = itemView.findViewById<TextView>(R.id.monthName)
    private val monthTemperature = itemView.findViewById<TextView>(R.id.monthTemperature)
    var month: Month? = null
    fun bindTo(month: Month?) {
        this.month = month
        monthName.text = month?.toString()

        // TODO set temperature by settings of the temperature scales
    }
}
