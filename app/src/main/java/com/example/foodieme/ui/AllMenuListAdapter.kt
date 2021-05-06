package com.example.foodieme.ui.allmenulistadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieme.R
import com.example.foodieme.databinding.HomeScreenItemAllBinding
import com.example.foodieme.domain.FlowsMenu

class AllMenuListAdapter (val clickListener: MenuClickListener) :RecyclerView.Adapter<AllItemViewHolder>(){

    var allItem : List<FlowsMenu> = emptyList()
    set(value) {
        field = value

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val withDatabinding: HomeScreenItemAllBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AllItemViewHolder.LAYOUT,
            parent,
            false
        )
        return AllItemViewHolder(withDatabinding)

    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        holder.homeScreenItemAllBinding.also {
            it.chipFlowMenu = allItem[position]
            it.clickListener = clickListener
        }


    }

    override fun getItemCount() = allItem.size

}




class MenuClickListener (val clickListener: (FlowsMenu)-> Unit){
    fun onClick(flowsMenu: FlowsMenu) = clickListener(flowsMenu)

}


class AllItemViewHolder(val homeScreenItemAllBinding: HomeScreenItemAllBinding) : RecyclerView.ViewHolder(homeScreenItemAllBinding.root){

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_screen_item_all

    }
}
