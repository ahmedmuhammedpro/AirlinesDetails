package com.ahmed.airlinesdetails.main_view.airlines_listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesmodel.entities.Airline

class AirlinesAdapter(val items: ArrayList<Airline>, private val onItemClick: OnItemClick)
    : RecyclerView.Adapter<AirlinesAdapter.AirlineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirlineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.airline_view_item, parent, false)
        val viewHolder = AirlineViewHolder(view)
        view.setOnClickListener {
            onItemClick.onItemClick(it, items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: AirlineViewHolder, position: Int) {
        holder.airlineTextView.text = items[position].name
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class AirlineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val airlineTextView: TextView = view.findViewById(R.id.airline_text_view)
        val airlineDetailsButton: ImageView = view.findViewById(R.id.airline_details_image)
    }

}