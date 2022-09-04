package com.example.bikes.fragment.dashboard

import androidx.lifecycle.ViewModelProvider
import com.example.bikes.ContextAwareApplication
import com.example.bikes.R
import com.example.bikes.fragment.BaseFragment
import com.example.bikes.fragment.DashboardViewModel

class DashboardFragment : BaseFragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private lateinit var viewModel: DashboardViewModel

    override var layoutID: Int = R.layout.dashboard_fragment

    override fun initViewModels() {
        viewModel = ViewModelProvider(this).get<DashboardViewModel>(DashboardViewModel::class.java)
        viewModel.initAPI(ContextAwareApplication.applicationContext(), "pub_transport", "stacje_rowerowe")
    }

    override fun initBinding() {
    }

    override fun initObservers() {
    }

    override fun fragmentTag(): String {
        return "DashboardFragment"
    }

}