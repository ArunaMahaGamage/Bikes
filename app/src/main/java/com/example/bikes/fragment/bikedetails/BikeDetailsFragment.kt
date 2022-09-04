package com.example.bikes.fragment.bikedetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.bikes.ContextAwareApplication
import com.example.bikes.R
import com.example.bikes.databinding.BikeDetailsFragmentBinding
import com.example.bikes.fragment.BaseFragment
import com.example.bikes.viewmodel.BikeDetailsViewModel

class BikeDetailsFragment : BaseFragment() {

    companion object {
        fun newInstance() = BikeDetailsFragment()
    }

    private lateinit var viewModel: BikeDetailsViewModel
    private lateinit var mBinding: BikeDetailsFragmentBinding
    override var layoutID: Int = R.layout.bike_details_fragment


    override fun initViewModels() {
        viewModel = ViewModelProvider(requireActivity()).get<BikeDetailsViewModel>(BikeDetailsViewModel::class.java)
    }

    override fun initBinding() {
        mBinding = mBindingRoot as BikeDetailsFragmentBinding
//        mBinding.viewmodel = viewModel
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

}