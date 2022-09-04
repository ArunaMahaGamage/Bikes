package com.example.bikes.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikes.api.ApiClient
import com.example.bikes.api.ApiService
import com.example.bikes.fragment.dashboard.DashboardFragment
import com.example.bikes.model.BikeStationsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {

    var mutableLiveDataResult = MutableLiveData<BikeStationsResponse>()

    private var apiInterface: ApiService? = null

    fun initAPI(context: Context, mtype: String, co: String) {

        apiInterface = ApiClient.getApiClient(context).create(ApiService::class.java)
        val call: Call<BikeStationsResponse> = apiInterface!!.getBikeStations(mtype, co)

        call.enqueue(object : Callback<BikeStationsResponse?> {
            override fun onResponse(
                call: Call<BikeStationsResponse?>,
                response: Response<BikeStationsResponse?>
            ) {
                val success = response.isSuccessful()
                if (success) {
                    var responseResult = response.body()
                    mutableLiveDataResult.value = responseResult!!
                    Log.e("message", response.message().toString())
                } else {
                    mutableLiveDataResult.removeObservers(context as DashboardFragment)
                }
            }

            override fun onFailure(call: Call<BikeStationsResponse?>, t: Throwable) {
                Log.e("message", "Error")
            }
        })
    }
}