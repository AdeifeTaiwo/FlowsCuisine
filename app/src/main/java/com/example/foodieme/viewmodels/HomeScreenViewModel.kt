package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase.Companion.getInstance
import com.example.foodieme.database.getDatabase
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.repository.FlowsMenuRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {




    private val database = getDatabase(application)
    private val database2 = getInstance(application)
    private val menuRepository = FlowsMenuRepository(database, database2)


    init{
        viewModelScope.launch {
            menuRepository.refreshFlowsMenu()
        }
    }

    val popularFlowsMenu = menuRepository.popularFlowsMenu





    fun onQueryChanged(filter: String?) : LiveData<List<FlowsMenu>>{
       return menuRepository.getFlowMenuByCategory(filter)
    }


    private val _navigateToDetailScreen = MutableLiveData<FlowsMenu?>()

    val navigateToDetailScreen : LiveData<FlowsMenu?>
        get() = _navigateToDetailScreen

    fun onDetailScreenClicked(flowsMenu: FlowsMenu) {
        _navigateToDetailScreen.value = flowsMenu
    }

    fun onDetailScreenNavigated() {
        _navigateToDetailScreen.value = null
    }









}