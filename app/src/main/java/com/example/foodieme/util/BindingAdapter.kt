package com.example.foodieme.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.foodieme.GlideApp
import com.example.foodieme.GlideAppCustom


@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String){

    url.let {
        val imgUri =url.toUri().buildUpon().scheme("https").build()
        GlideApp.with(imageView.context).load(url).into(imageView)
    }


}