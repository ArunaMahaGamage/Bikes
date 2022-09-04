package com.example.bikes.fragment.dashboard

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bikes.ContextAwareApplication
import com.example.bikes.R
import com.example.bikes.adapter.BikeStationsAdapter
import com.example.bikes.databinding.DashboardFragmentBinding
import com.example.bikes.fragment.BaseFragment
import com.example.bikes.viewmodel.DashboardViewModel

class DashboardFragment : BaseFragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private lateinit var viewModel: DashboardViewModel
    private lateinit var mBinding: DashboardFragmentBinding
    private lateinit var adapter: BikeStationsAdapter

    override var layoutID: Int = R.layout.dashboard_fragment


    override fun initViewModels() {
        viewModel = ViewModelProvider(this).get<DashboardViewModel>(DashboardViewModel::class.java)
        viewModel.initAPI(ContextAwareApplication.applicationContext(), "pub_transport", "stacje_rowerowe")
    }

    override fun initBinding() {
        mBinding = mBindingRoot as DashboardFragmentBinding

        adapter = BikeStationsAdapter(requireContext(), onClickListener = this::onClickListener, onClickListenerInfo = this::onClickListenerInfo)
        mBinding.bikeStationsRecyclerView.adapter = adapter
        mBinding.bikeStationsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun initObservers() {
        viewModel.mutableLiveDataResult.observe(this, Observer {
            adapter.updateListItems(it)
        })
    }

    override fun fragmentTag(): String {
        return "DashboardFragment"
    }

    private fun onClickListener(view: View, position: Int) {
//        navigateTo(R.id.action_medical_specialties_fragment_to_doctorsFragment)
//        startNewsDetails(position)
    }

    private fun onClickListenerInfo(view: View, position: Int) {

    }
}