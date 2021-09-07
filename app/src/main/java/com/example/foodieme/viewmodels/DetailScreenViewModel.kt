package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.foodieme.database.checkoutdatabase.Checkout
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.repository.MainMainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class DetailScreenViewModel constructor(
    private val mainRepository: MainMainRepository,
    private val checkoutDatabaseDao: CheckoutDatabaseDao,

    private val flowsMenu: FlowsMenu,
) : ViewModel() {

    //@Inject lateinit var mainRepository: MainMainRepository
    //@Inject lateinit var checkoutDatabaseDao: CheckoutDatabaseDao


    private var latestAddition = MutableLiveData<Checkout?>()


    private val _price = MutableLiveData<Double>()

    val price: LiveData<Double>
        get() = _price


    private val _quantity = MutableLiveData<Int>()

    val quantity: LiveData<Int>
        get() = _quantity


    private val _selectedMenu = MutableLiveData<FlowsMenu>()

    val selectedMenu: LiveData<FlowsMenu>
        get() = _selectedMenu

    init {
        _selectedMenu.value = flowsMenu
        _price.value = flowsMenu.price!!
        _quantity.value = 1
    }

    fun add() {
        _quantity.value = (_quantity.value)?.plus(1)?.let { Math.abs(it) }
        _price.value = _quantity.value?.times(flowsMenu.price!!)
    }


    fun remove() {
        _quantity.value = (_quantity.value)?.minus(1)?.let { abs(it) }
        _price.value = flowsMenu.price?.let { _quantity.value?.times(it) }

    }


    /**
     * working with repository for the check out database
     */


    init {
        viewModelScope.launch {
            mainRepository.refreshFlowsMenu()

        }

        // TODO: TO BE SOON IMPLEMENTED
        //val checkoutPage = menuRepository.checkoutMenu
        initializeLatestAddition()

    }


    private fun initializeLatestAddition() {
        viewModelScope.launch {
            latestAddition.value = getLatestAdditonFromDatabase()
        }
    }

    private suspend fun getLatestAdditonFromDatabase(): Checkout? {
        return checkoutDatabaseDao.getLatestAddition()

    }


    fun addToCartButton() {
        viewModelScope.launch {
            if (_quantity.value!! >= 1) {
                val newCheckOut = Checkout()
                insert(newCheckOut)

                latestAddition.value = getLatestAdditonFromDatabase()

                onUpdateToCart()

            }
        }
    }

    private fun onUpdateToCart() {

        viewModelScope.launch {

            val toCart = latestAddition.value ?: return@launch

            toCart.imageUrl = flowsMenu.image!!
            toCart.priceInfo = flowsMenu.price!!
            toCart.weight = "220Kg"
            toCart.quantity = _quantity.value!!
            toCart.name = flowsMenu.name!!

            update(toCart)

            onDetailScreenClicked()

        }

    }

    private suspend fun insert(checkout: Checkout) {
        withContext(Dispatchers.IO) {
            checkoutDatabaseDao.insert(checkout)
        }
    }

    private suspend fun update(night: Checkout) {
        withContext(Dispatchers.IO) {
            checkoutDatabaseDao.update(night)
        }
    }

    private val _navigateToAddToCartScreen = MutableLiveData<Boolean?>()

    val navigateToAddToCartScreen: LiveData<Boolean?>
        get() = _navigateToAddToCartScreen

    private fun onDetailScreenClicked() {
        _navigateToAddToCartScreen.value = true
    }

    fun onDetailScreenNavigated() {
        _navigateToAddToCartScreen.value = null
    }

}