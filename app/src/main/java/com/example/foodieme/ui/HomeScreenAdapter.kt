package com.example.foodieme.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieme.R
import com.example.foodieme.databinding.HomeScreenItemPopularBinding
import com.example.foodieme.domain.FlowsMenu

class HomeScreenAdapter (val clickListener: FlowsMenuClickListener): RecyclerView.Adapter<FlowsMenuViewHolder>(){

    var popular: List<FlowsMenu> = emptyList()
    set(value) {
        field = value

        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowsMenuViewHolder {
        val withDataBinding: HomeScreenItemPopularBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            FlowsMenuViewHolder.LAYOUT,
            parent,
            false)
        return FlowsMenuViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: FlowsMenuViewHolder, position: Int) {
        holder.viewPopularDataBinding.also {
            it.popular = popular[position]
            it.popularClickListener = clickListener
        }
    }

    override fun getItemCount(): Int = popular.size
}

class FlowsMenuClickListener(val clickListener: (FlowsMenu) -> Unit) {

    fun onClick(flowsMenu: FlowsMenu) = clickListener(flowsMenu)


}

class FlowsMenuViewHolder (val viewPopularDataBinding: HomeScreenItemPopularBinding): RecyclerView.ViewHolder(viewPopularDataBinding.root){
companion object{
    @LayoutRes
    val LAYOUT = R.layout.home_screen_item_popular
}
}
