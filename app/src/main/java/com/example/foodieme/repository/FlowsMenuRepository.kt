package com.example.foodieme.repository;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.CoroutinesRoom
import com.example.foodieme.database.FlowsMenuDatabase;
import com.example.foodieme.database.asDomainModel
import com.example.foodieme.database.checkoutdatabase.Checkout
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase
import com.example.foodieme.database.checkoutdatabase.asDomainModel
import com.example.foodieme.domain.CheckoutMenu
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.network.Network
import com.example.foodieme.network.NetworkFlowsMenu
import com.example.foodieme.network.asDatabaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FlowsMenuRepository (private val database: FlowsMenuDatabase, private val database2: CheckoutDatabase) {



    val flowsMenu: LiveData<List<FlowsMenu>> = Transformations.map(database.flowsMenuDao.getFlowsMenu()){
        it.asDomainModel()
    }

    val popularFlowsMenu: LiveData<List<FlowsMenu>> = Transformations.map(database.flowsMenuDao.getPopularFlowsMenu("popular")) {
        it.asDomainModel()

    }

    //checkout adapter data
    // TODO: TO BE SOON IMPLEMENTED
    val checkoutMenu: LiveData<List<CheckoutMenu>> = Transformations.map(database2.checkoutDatabaseDao.getAllNights()){
        it.asDomainModel()
    }

    val activeOrder: LiveData<List<CheckoutMenu>> = Transformations.map(database2.checkoutDatabaseDao.getActiveOrders(true)){
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












    val _getDatabase = MutableLiveData<List<NetworkFlowsMenu>>()
    val getDatabase : LiveData<List<NetworkFlowsMenu>>
    get() = _getDatabase


    suspend fun refreshFlowsMenu(){

            val call = Network.retrofitService.getPlaylist()
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
                        database.flowsMenuDao.insertAll(*newList)
                    }
                }

            }


    }



