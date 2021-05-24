package com.example.foodieme.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DeliveryPageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DeliveryPageViewModel::class.java)){
            return DeliveryPageViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}