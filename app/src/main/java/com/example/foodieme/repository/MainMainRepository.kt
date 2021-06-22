package com.example.foodieme.repository

import androidx.lifecycle.LiveData
import com.example.foodieme.domain.CheckoutMenu
import com.example.foodieme.domain.FlowsMenu


interface MainMainRepository {

     fun returnPopularFlowMenu(): LiveData<List<FlowsMenu>>

     fun getFlowMenuByCategory(filter: String?): LiveData<List<FlowsMenu>>

     fun chipFlowMenu() : LiveData<List<FlowsMenu>>

     fun returnCheckOutMenu() : LiveData<List<CheckoutMenu>>

     fun returnActiveOrdersInCheckOutMenu() : LiveData<List<CheckoutMenu>>

     suspend fun refreshFlowsMenu()

}