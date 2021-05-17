package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddToCartViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked cast")
        if(modelClass.isAssignableFrom(AddToCartViewModel::class.java)){
            return AddToCartViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}