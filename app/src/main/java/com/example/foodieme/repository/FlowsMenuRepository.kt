package com.example.foodieme.repository;

import androidx.lifecycle.LiveData

import androidx.lifecycle.Transformations
import com.example.foodieme.database.FlowsMenuDao

import com.example.foodieme.database.asDomainModel

import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao
import com.example.foodieme.database.checkoutdatabase.asDomainModel
import com.example.foodieme.domain.CheckoutMenu
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.network.FlowsMenuService
import com.example.foodieme.network.NetworkFlowsMenu
import com.example.foodieme.network.asDatabaseModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class FlowsMenuRepository @Inject constructor() : MainMainRepository {

    @Inject lateinit var flowsMenuDao: FlowsMenuDao
    @Inject lateinit var checkoutDatabaseDao: CheckoutDatabaseDao
    @Inject lateinit var  service: FlowsMenuService




    override fun getFlowMenuByCategory(filter: String?): LiveData<List<FlowsMenu>> {

        return when (filter) {
            null -> chipFlowMenu()
            else -> Transformations.map(flowsMenuDao.getSnackFlowsMenu(filter)) {
                it.asDomainModel()
            }
        }
    }

    override fun chipFlowMenu(): LiveData<List<FlowsMenu>> {
        return Transformations.map(flowsMenuDao.getFlowsMenu()) {
            it.asDomainModel()
        }
    }

    override fun returnPopularFlowMenu(): LiveData<List<FlowsMenu>> {
        return Transformations.map(flowsMenuDao.getPopularFlowsMenu("popular")) {
            it.asDomainModel()
        }
    }

    override fun returnCheckOutMenu(): LiveData<List<CheckoutMenu>> {
        return Transformations.map(checkoutDatabaseDao.getAllNights()) {
            it.asDomainModel()
        }
    }

    override fun returnActiveOrdersInCheckOutMenu(): LiveData<List<CheckoutMenu>> {
        return Transformations.map(checkoutDatabaseDao.getActiveOrders(true)) {
            it.asDomainModel()
        }
    }


    override suspend fun refreshFlowsMenu() {
            val call = service.getPlaylist()
            val response = suspendCoroutine<Response<List<NetworkFlowsMenu>>> { continuation ->
                call.enqueue(object : Callback<List<NetworkFlowsMenu>> {

                    override fun onResponse(
                        call: Call<List<NetworkFlowsMenu>>,
                        response: Response<List<NetworkFlowsMenu>>
                    ) {
                        continuation.resume(response)
                    }

                    override fun onFailure(call: Call<List<NetworkFlowsMenu>>, t: Throwable) {}
                })
            }
            val newList = response.body()?.asDatabaseModel()
            if (newList != null) {
                withContext(Dispatchers.IO) {
                    flowsMenuDao.insertAll(*newList)
                }
            }
        }




    }


