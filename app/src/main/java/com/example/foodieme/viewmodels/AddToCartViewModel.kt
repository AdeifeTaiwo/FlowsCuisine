package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.foodieme.database.checkoutdatabase.Checkout
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase
import com.example.foodieme.database.getDatabase
import com.example.foodieme.repository.FlowsMenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddToCartViewModel (application: Application): AndroidViewModel(application) {






    var totalPrice: Double = 0.0


    private val database = getDatabase(application)
    private val database2 = CheckoutDatabase.getInstance(application)
    private val menuRepository = FlowsMenuRepository(database, database2)

     var checkoutPage = menuRepository.checkoutMenu




    init {
        viewModelScope.launch {
            menuRepository.refreshFlowsMenu()


        }

    }





    fun onClearWithId(checkoutId: Long){
        viewModelScope.launch {

          clearWithId(checkoutId)

        }

    }

    private suspend fun clearWithId(key: Long){
        withContext(Dispatchers.IO){
            database2.checkoutDatabaseDao.clearWithId(key)

        }
    }

    fun totalPrice2() =Transformations.map(checkoutPage){
        totalPrice = 0.0
        it.forEach {
            totalPrice += it.priceInfo * it.quantity
        }
        return@map totalPrice
    }

    fun totalPriceWithDelivery() =Transformations.map(checkoutPage){
        totalPrice = 0.0
        it.forEach {
            totalPrice += it.priceInfo * it.quantity
        }
        return@map totalPrice + 500.0
    }




    fun onAddWithId(id: Long) {
        viewModelScope.launch {

            updateToAddQuantity(id)
        }

    }
    private suspend fun updateToAddQuantity(id: Long){
        withContext(Dispatchers.IO){
            val toAdd = database2.checkoutDatabaseDao.getNightWithId(id)
            toAdd.quantity += 1
            database2.checkoutDatabaseDao.update(toAdd)
        }
    }

    fun onSubtractWithId(id: Long) {
        viewModelScope.launch {

            updateToSubtractQuantity(id)
        }

    }
    private suspend fun updateToSubtractQuantity(id: Long){
        withContext(Dispatchers.IO){
            val toAdd = database2.checkoutDatabaseDao.getNightWithId(id)

            if(toAdd.quantity>0){
            toAdd.quantity -= 1
            }
            database2.checkoutDatabaseDao.update(toAdd)
        }
    }

    fun onCheckoutClick(){

    }


}






