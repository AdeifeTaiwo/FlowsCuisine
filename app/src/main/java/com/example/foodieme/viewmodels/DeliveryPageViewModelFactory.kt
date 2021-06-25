package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao
import com.example.foodieme.database.timedurationdatabase.TimeDurationDao
import com.example.foodieme.repository.MainMainRepository

class DeliveryPageViewModelFactory(private val mainRepository: MainMainRepository,
                                   private  val timeDurationDao: TimeDurationDao,
                                   private val checkoutDatabaseDao: CheckoutDatabaseDao,
                                   private  val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DeliveryPageViewModel::class.java)){
            return DeliveryPageViewModel(mainRepository, timeDurationDao, checkoutDatabaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}