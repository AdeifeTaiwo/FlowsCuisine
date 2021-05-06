package com.example.foodieme.repository;

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.foodieme.database.DatabaseFlowsMenu
import com.example.foodieme.database.FlowsMenuDatabase;
import com.example.foodieme.database.asDomainModel
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.network.Network
import com.example.foodieme.network.NetworkFlowsMenu
import com.example.foodieme.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlowsMenuRepository (private val database: FlowsMenuDatabase) {



    val flowsMenu: LiveData<List<FlowsMenu>> = Transformations.map(database.flowsMenuDao.getFlowsMenu()){
        it.asDomainModel()
    }

    val popularFlowsMenu: LiveData<List<FlowsMenu>> = Transformations.map(database.flowsMenuDao.getPopularFlowsMenu("popular")) {
        it.asDomainModel()

    }

    fun getFlowMenuByCategory(filter: String?) : LiveData<List<FlowsMenu>> {

        return when(filter) {
            null -> flowsMenu
            else -> Transformations.map(database.flowsMenuDao.getSnackFlowsMenu(filter)) {
                it.asDomainModel()
            }
        }
    }















    suspend fun refreshFlowsMenu(){
        withContext(Dispatchers.IO){
            val playlist = Network.retrofitService.getPlaylist().await()

                database.flowsMenuDao.insertAll(*playlist.asDatabaseModel())

        }
    }




}
