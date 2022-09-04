package com.example.bikes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bikes.R
import com.example.bikes.model.bikestations.BikeStationsResponse

class BikeStationsAdapter // Provide a suitable constructor (depends on the kind of dataset)
    (private val context: Context, private val onClickListener: (View, Int) -> Unit, private val onClickListenerInfo: (View, Int) -> Unit) :
    RecyclerView.Adapter<BikeStationsAdapter.ViewHolder>() {

    private lateinit var bikeStationsResponse: BikeStationsResponse

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var stationsConstraintLayout: ConstraintLayout
        var titleAdapterTextView: TextView
        var bikeCountTextView: TextView
        var bgImage: ImageView

        init {
            stationsConstraintLayout = v.findViewById(R.id.stationsConstraintLayout)
            titleAdapterTextView = v.findViewById(R.id.titleAdapterTextView)
            bikeCountTextView = v.findViewById(R.id.bikeCountTextView)
            bgImage = v.findViewById(R.id.bikeImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bike_stations, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleAdapterTextView.text = bikeStationsResponse.features[position].id + " " + bikeStationsResponse.features[position].properties.label
        holder.bikeCountTextView.text = bikeStationsResponse.features[position].properties.free_racks
        holder.stationsConstraintLayout.setOnClickListener {view ->onClickListener.invoke(view, position)}

        Glide.with(context)
            .load(R.drawable.ic_bike)
//            .load(bikeStationsResponse.features[position].urlToImage)
            .centerCrop()
            .placeholder(R.drawable.background_gradient)
            .into(holder.bgImage)
    }

    override fun getItemCount(): Int {
        if (this::bikeStationsResponse.isInitialized) {
            return bikeStationsResponse.features.size
        }
        return 0
    }

    fun updateListItems(bikeStationsResponse: BikeStationsResponse) {
        this.bikeStationsResponse = bikeStationsResponse
        notifyDataSetChanged()
    }
}