package com.example.bikes.fragment.bikedetails

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bikes.ContextAwareApplication
import com.example.bikes.R
import com.example.bikes.databinding.BikeDetailsFragmentBinding
import com.example.bikes.fragment.BaseFragment
import com.example.bikes.viewmodel.BikeDetailsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class BikeDetailsFragment : BaseFragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = BikeDetailsFragment()
    }

    private lateinit var viewModel: BikeDetailsViewModel
    private lateinit var mBinding: BikeDetailsFragmentBinding
    private var mMap: GoogleMap? = null

    override var layoutID: Int = R.layout.bike_details_fragment


    override fun initViewModels() {
        viewModel = ViewModelProvider(requireActivity()).get<BikeDetailsViewModel>(BikeDetailsViewModel::class.java)
    }

    override fun initBinding() {
        mBinding = mBindingRoot as BikeDetailsFragmentBinding
//        mBinding.viewmodel = viewModel

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun initObservers() {
        viewModel.mutableLiveDataResult.observe(this, Observer {
            mBinding.viewmodel = viewModel
//            mBinding.viewmodel.mutableLiveDataResult.value = it
        })

        Glide.with(ContextAwareApplication.applicationContext())
            .load(R.drawable.ic_bike)
            .centerCrop()
            .placeholder(R.drawable.background_gradient)
            .into(mBinding.bikeImage)

        Glide.with(ContextAwareApplication.applicationContext())
            .load(R.drawable.ic_lock)
            .centerCrop()
            .placeholder(R.drawable.background_gradient)
            .into(mBinding.lockImage)
    }

    override fun fragmentTag(): String {
        return "BikeDetailsFragment"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        // Add a marker in Sydney and move the camera
        var latitude = viewModel.mutableLiveDataResult.value!!.geometry.coordinates[0].toDouble()
        var longitude = viewModel.mutableLiveDataResult.value!!.geometry.coordinates[1].toDouble()
        val TutorialsPoint = LatLng(latitude, longitude)
        mMap!!.addMarker(MarkerOptions().position(TutorialsPoint).title(viewModel.mutableLiveDataResult.value!!.properties.label))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint))
    }

}