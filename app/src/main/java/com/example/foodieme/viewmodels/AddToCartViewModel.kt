package com.example.foodieme.viewmodels

import android.app.Application

import androidx.lifecycle.*

import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao

import com.example.foodieme.domain.CheckoutMenu

import com.example.foodieme.repository.MainMainRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject



class AddToCartViewModel constructor(private val mainRepository: MainMainRepository,
                                     private val checkoutDatabaseDao: CheckoutDatabaseDao,
                                     application: Application): AndroidViewModel(application) {



    //@Inject  lateinit var mainRepository: MainMainRepository
    //@Inject  lateinit var checkoutDatabaseDao: CheckoutDatabaseDao

    var totalPrice: Double = 0.0




     var checkoutPage = mainRepository.returnCheckOutMenu()




    init {
        viewModelScope.launch {
            mainRepository.refreshFlowsMenu()


        }

    }





    fun onClearWithId(checkoutId: Long){
        viewModelScope.launch {

          clearWithId(checkoutId)

        }

    }

    private suspend fun clearWithId(key: Long){
        withContext(Dispatchers.IO){
            if(!checkoutDatabaseDao.getNightWithId(key).OrderIsActive)
            checkoutDatabaseDao.clearWithId(key)

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
            val toAdd = checkoutDatabaseDao.getNightWithId(id)
            toAdd.quantity += 1
            checkoutDatabaseDao.update(toAdd)
        }
    }

    fun onSubtractWithId(id: Long) {
        viewModelScope.launch {

            updateToSubtractQuantity(id)
        }

    }

    private suspend fun updateToSubtractQuantity(id: Long){
        withContext(Dispatchers.IO){
            val toAdd = checkoutDatabaseDao.getNightWithId(id)

            if(toAdd.quantity>0){
            toAdd.quantity -= 1
            }
            checkoutDatabaseDao.update(toAdd)
        }
    }


    private fun updateTheState(){
        viewModelScope.launch {
            updateState()
        }
    }
    private suspend fun updateState(){
        withContext(Dispatchers.IO){
            val state = checkoutDatabaseDao.getAllNightsNonLivedata()
            state.forEach {
                val eachState = it
                eachState.OrderIsActive = true
                checkoutDatabaseDao.update(eachState)
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






