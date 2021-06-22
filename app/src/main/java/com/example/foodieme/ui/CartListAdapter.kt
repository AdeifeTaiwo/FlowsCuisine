package com.example.foodieme.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieme.R

import com.example.foodieme.databinding.ListRecyclerViewBinding
import com.example.foodieme.domain.CheckoutMenu
import javax.inject.Inject

class CartListAdapter @Inject constructor(): RecyclerView.Adapter<MyViewHolder>(){

    var checkoutMenu: List<CheckoutMenu> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val withDatabinding: ListRecyclerViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            MyViewHolder.LAYOUT,
            parent,
            false
        )
        return MyViewHolder(withDatabinding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.listRecyclerViewBinding.also {
            it.cartCheckout2= checkoutMenu[position]




        }


    }
    override fun getItemCount() = checkoutMenu.size

}



class MyViewHolder(val listRecyclerViewBinding: ListRecyclerViewBinding) : RecyclerView.ViewHolder(listRecyclerViewBinding.root){

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_recycler_view

    }
}
