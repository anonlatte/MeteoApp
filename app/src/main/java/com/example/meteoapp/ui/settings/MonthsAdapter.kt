package com.example.meteoapp.ui.settings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.db.model.Month

class MonthsAdapter(private val months: Array<Month>) : RecyclerView.Adapter<MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MonthViewHolder(parent)

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bindTo(months[position])
    }

    override fun getItemCount(): Int = months.size
}