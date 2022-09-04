package com.example.bikes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikes.model.bikestations.Features

class BikeDetailsViewModel : ViewModel() {
    var mutableLiveDataResult = MutableLiveData<Features>()
}