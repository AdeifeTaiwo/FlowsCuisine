package com.example.foodieme

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("setPrice")

fun TextView.setPrice( double: Double){
    text = context.getString(R.string.price, double)

}