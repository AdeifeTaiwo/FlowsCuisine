package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao
import com.example.foodieme.repository.FlowsMenuRepository
import com.example.foodieme.repository.MainMainRepository

class AddToCartViewModelFactory(private val mainRepository: MainMainRepository,
                                private val checkoutDatabaseDao: CheckoutDatabaseDao,
                                private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked cast")
        if(modelClass.isAssignableFrom(AddToCartViewModel::class.java)){
            return AddToCartViewModel(mainRepository, checkoutDatabaseDao, application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}