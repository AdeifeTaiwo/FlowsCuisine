package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodieme.domain.FlowsMenu

class DetailScreenViewModelFactory (private  val application: Application,
                                    private val flowsMenu: FlowsMenu) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked cast")

        if (modelClass.isAssignableFrom(DetailScreenViewModel::class.java)) {
            return DetailScreenViewModel(application, flowsMenu) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}