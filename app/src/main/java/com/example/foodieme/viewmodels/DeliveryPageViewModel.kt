package com.example.foodieme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodieme.domain.FlowsMenu

class DeliveryPageViewModel : ViewModel() {



    private val _distance = MutableLiveData<String>()

    val distance: LiveData<String?>
    get() = _distance


    fun setDistance(distance: String) {
        _distance.value = distance
    }

}