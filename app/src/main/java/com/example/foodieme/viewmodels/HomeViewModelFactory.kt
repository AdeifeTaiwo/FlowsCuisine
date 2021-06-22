package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodieme.database.timedurationdatabase.TimeDurationDao
import com.example.foodieme.repository.MainMainRepository

class HomeViewModelFactory(private val menuRepository: MainMainRepository,
                           private  val timeDurationDao: TimeDurationDao,
                           val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(menuRepository,timeDurationDao, app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")

    }


}

