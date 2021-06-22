package com.example.foodieme.repository;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.CoroutinesRoom
import com.example.foodieme.database.FlowsMenuDao
import com.example.foodieme.database.FlowsMenuDatabase;
import com.example.foodieme.database.asDomainModel
import com.example.foodieme.database.checkoutdatabase.Checkout
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao
import com.example.foodieme.database.checkoutdatabase.asDomainModel
import com.example.foodieme.domain.CheckoutMenu
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.network.FlowsMenuService
import com.example.foodieme.network.NetworkFlowsMenu
import com.example.foodieme.network.asDatabaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FlowsMenuRepository @Inject constructor( private val flowsMenuDao: FlowsMenuDao,
                                               private val checkoutDatabaseDao: CheckoutDatabaseDao,
                                               private val service: FlowsMenuService) {



    val flowsMenu: LiveData<List<FlowsMenu>> = Transformations.map(flowsMenuDao.getFlowsMenu()){
        it.asDomainModel()
    }

    val popularFlowsMenu: LiveData<List<FlowsMenu>> = Transformations.map(flowsMenuDao.getPopularFlowsMenu("popular")) {
        it.asDomainModel()

    }

    //checkout adapter data
    // TODO: TO BE SOON IMPLEMENTED
    val checkoutMenu: LiveData<List<CheckoutMenu>> = Transformations.map(checkoutDatabaseDao.getAllNights()){
        it.asDomainModel()
    }

    val activeOrder: LiveData<List<CheckoutMenu>> = Transformations.map(checkoutDatabaseDao.getActiveOrders(true)){
        it.asDomainModel()
    }





    fun getFlowMenuByCategory(filter: String?) : LiveData<List<FlowsMenu>> {

        return when(filter) {
            null -> flowsMenu
            else -> Transformations.map(flowsMenuDao.getSnackFlowsMenu(filter)) {
                it.asDomainModel()
            }
        }
    }



    suspend fun refreshFlowsMenu(){

            val call = service.getPlaylist()
                    val response = suspendCoroutine<Response<List<NetworkFlowsMenu>>>{ continuation ->
                        call. enqueue(object : Callback<List<NetworkFlowsMenu>> {


                            override fun onResponse(
                                call: Call<List<NetworkFlowsMenu>>, response: Response<List<NetworkFlowsMenu>>
                            ) {
                                    continuation.resume(response)
                            }

                            override fun onFailure(call: Call<List<NetworkFlowsMenu>>, t: Throwable) {}

                        })

                    }
                val newList = response.body()?.asDatabaseModel()
                if(newList!= null){
                    withContext(Dispatchers.IO){
                        flowsMenuDao.insertAll(*newList)
                    }
                }

            }


    }



