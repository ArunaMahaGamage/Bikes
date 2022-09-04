package com.example.bikes.fragment.bikedetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bikes.R
import com.example.bikes.viewmodel.BikeDetailsViewModel

class BikeDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = BikeDetailsFragment()
    }

    private lateinit var viewModel: BikeDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bike_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BikeDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}