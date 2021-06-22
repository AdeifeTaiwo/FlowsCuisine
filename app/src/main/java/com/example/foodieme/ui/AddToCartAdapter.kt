package com.example.foodieme.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieme.R
import com.example.foodieme.databinding.CartScreenBinding
import com.example.foodieme.domain.CheckoutMenu
import javax.inject.Inject

class AddToCartAdapter @Inject constructor(
    private val clickListener: AddToCartClickListener,
    private val addToQuantityClickListener: AddToQuantityClickListener,
    private val subtractFromQuantityClickListener: SubtractFromQuantityClickListener) :RecyclerView.Adapter<AllItemViewHolder>(){

    var checkoutMenu: List<CheckoutMenu> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val withDatabinding: CartScreenBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AllItemViewHolder.LAYOUT,
            parent,
            false
        )
        return AllItemViewHolder(withDatabinding)

    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        holder.cartScreenBinding.also {
            it.cartCheckout= checkoutMenu[position]
            it.addListiner = addToQuantityClickListener
            it.cartClickListener = clickListener
            it.removeListiner = subtractFromQuantityClickListener



        }


    }

    override fun getItemCount() = checkoutMenu.size

}




class AddToCartClickListener (val clickListener: (CheckoutMenu: Long)-> Unit){
    fun onClick(checkoutId: Long) = clickListener(checkoutId)

}

class AddToQuantityClickListener(val addclickListener: (checkout: Long) -> Unit){
    fun onAddClick(checkout: Long) = addclickListener(checkout)
}

class SubtractFromQuantityClickListener(val addclickListener: (checkout: Long) -> Unit){
    fun onSubtractClick(checkout: Long) = addclickListener(checkout)
}


class AllItemViewHolder(val cartScreenBinding: CartScreenBinding) : RecyclerView.ViewHolder(cartScreenBinding.root){

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.cart_screen

    }
}
