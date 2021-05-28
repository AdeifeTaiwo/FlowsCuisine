package com.example.foodieme.util

import android.graphics.DiscretePathEffect
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.foodieme.GlideApp
import com.example.foodieme.GlideAppCustom
import com.example.foodieme.R
import com.example.foodieme.domain.CheckoutMenu
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.ui.CartListAdapter
import com.example.foodieme.ui.allmenulistadapter.AllMenuListAdapter
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*



@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    GlideApp.with(imageView.context).load(url).into(imageView)
}

@BindingAdapter("setPrice")
fun TextView.setPrice(double: Double?){
    text = context.resources.getString(R.string.price, double.toString())

}

@BindingAdapter("elapsedTime")
fun TextView.setElapsedTime(value: Long) {
    val seconds = value / 1000

    text = if (seconds < 60) seconds.toString() else DateUtils.formatElapsedTime(seconds)
    isClickable = seconds <= 0L

}



@BindingAdapter("delivered_or_arrived")
fun TextView.setDeliveredOrArrived(value: Long){
    val seconds = value / 1000
    if(seconds  <= 0L){
        text = "Arrived"

    }
    else
        text = "Delivered"
}

@BindingAdapter("hide_linear_layout")
fun LinearLayout.hideLinearLayout(value:Long){
    val seconds = value / 1000
    if(seconds  <= 0L){
        visibility = View.GONE

    }
    else
        visibility = View.VISIBLE
}


@BindingAdapter ("hide_all_view")
fun View.hideAllLayout(checkoutMenu: LiveData<List<CheckoutMenu>>) {
    Transformations.map(checkoutMenu){
        if(it.isEmpty()) {
            visibility = View.GONE

        }
    }


}




