package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieme.database.getDatabase
import com.example.foodieme.repository.FlowsMenuRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val menuRepository = FlowsMenuRepository(database)

    init{
        viewModelScope.launch {
            menuRepository.refreshFlowsMenu()
        }
    }

    val flowsMenu = menuRepository.flowsMenu
    val popularFlowsMenu = menuRepository.popularFlowsMenu
}