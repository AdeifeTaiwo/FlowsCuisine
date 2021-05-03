package com.example.foodieme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodieme.domain.FlowsMenu

class DetailScreenViewModel (private  val flowsMenu: FlowsMenu) : ViewModel() {

    private val _price = MutableLiveData<Double>()

    val price: LiveData<Double>
    get() = _price


    private val _quantity = MutableLiveData<Int>()

    val quantity: LiveData<Int>
        get() = _quantity


    private val _selectedMenu = MutableLiveData<FlowsMenu>()

    val selectedMenu : LiveData<FlowsMenu>
    get() = _selectedMenu

    init {
        _selectedMenu.value = flowsMenu
        _price.value = flowsMenu.price
        _quantity.value = 1
    }

    fun add(){
        _quantity.value = (_quantity.value)?.plus(1)
        _price.value = _quantity.value?.let { (_price.value)?.times(it) }
    }


    fun remove(){
        _quantity.value = (_quantity.value)?.minus(1)
        _price.value = _quantity.value?.let { (_price.value)?.times(it) }
    }


}