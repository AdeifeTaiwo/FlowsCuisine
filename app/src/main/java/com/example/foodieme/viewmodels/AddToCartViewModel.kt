package com.example.foodieme.viewmodels

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.foodieme.database.checkoutdatabase.Checkout
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase
import com.example.foodieme.database.getDatabase
import com.example.foodieme.domain.CheckoutMenu
import com.example.foodieme.repository.FlowsMenuRepository
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_presentation.RavePayManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


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
            if(!database2.checkoutDatabaseDao.getNightWithId(key).OrderIsActive)
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


    private fun updateTheState(){
        viewModelScope.launch {
            updateState()
        }
    }
    private suspend fun updateState(){
        withContext(Dispatchers.IO){
            val state = database2.checkoutDatabaseDao.getAllNightsNonLivedata()
            state.forEach {
                val eachState = it
                eachState.OrderIsActive = true
                database2.checkoutDatabaseDao.update(eachState)
            }
        }
    }



    private val _navigateToToast = MutableLiveData<Boolean?>()

    val navigateToToast: LiveData<Boolean?>
    get() = _navigateToToast

    private fun navigateToToast(){
        _navigateToToast.value = true
    }

    fun onToastNavigated(){
        _navigateToToast.value = null
    }

    private val _navigateToOrderScreen = MutableLiveData<List<CheckoutMenu?>?>()

    val navigateToAddToOrderScreen : LiveData<List<CheckoutMenu?>?>
        get() = _navigateToOrderScreen

    fun onOrderScreenClicked(menu: List<CheckoutMenu>) {

        if(menu.isNotEmpty()) {
            //if last order isn't active yet, update it to be active
            if (!menu.last().isActive) {
                updateTheState()
                _navigateToOrderScreen.value = menu
            }
            else {
                navigateToToast()
            }
        }


    }

    fun onOrderScreenNavigated() {
        _navigateToOrderScreen.value = null
    }






}






