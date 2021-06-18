package com.ahmed.airlinesdetails.main.airlines_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.model.entities.Airline

class AirlinesAdapter(val items: ArrayList<Airline>) : RecyclerView.Adapter<AirlinesAdapter.AirlineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirlineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.airline_view_item, parent, false)
        return AirlineViewHolder(view)
    }

    override fun onBindViewHolder(holder: AirlineViewHolder, position: Int) {
        holder.airlineTextView.text = items[position].name
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class AirlineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val airlineTextView: TextView = view.findViewById(R.id.airline_text_view)
        val airlineDetailsButton: ImageButton = view.findViewById(R.id.airline_details_button)
    }

}