package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.repository.MainMainRepository

class DetailScreenViewModelFactory (private val mainRepository: MainMainRepository,
                                    private val checkoutDatabaseDao: CheckoutDatabaseDao,
                                    private val flowsMenu: FlowsMenu) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked cast")

        if (modelClass.isAssignableFrom(DetailScreenViewModel::class.java)) {
            return DetailScreenViewModel(mainRepository, checkoutDatabaseDao, flowsMenu) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}