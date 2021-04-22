package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodieme.database.getDatabase
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.repository.FlowsMenuRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {



    //private val _foodList = MutableLiveData<List<FlowsMenu>>()





    private var filter = FilterHolder()



    private val database = getDatabase(application)
    private val menuRepository = FlowsMenuRepository(database)


    var foodList: LiveData<List<FlowsMenu>> = menuRepository.flowsMenu

    init{
        viewModelScope.launch {
            menuRepository.refreshFlowsMenu()
        }
    }


    val popularFlowsMenu = menuRepository.popularFlowsMenu


    fun onQueryChanged(filter: String?) : LiveData<List<FlowsMenu>>{
       return menuRepository.getFlowMenuByCategory(filter)
    }



   







}