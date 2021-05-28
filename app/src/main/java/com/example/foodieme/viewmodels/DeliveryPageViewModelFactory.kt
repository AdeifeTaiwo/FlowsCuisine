package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DeliveryPageViewModelFactory(private  val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DeliveryPageViewModel::class.java)){
            return DeliveryPageViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}