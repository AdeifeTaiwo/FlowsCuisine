package com.example.foodieme.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.foodieme.GlideApp
import com.example.foodieme.GlideAppCustom
import com.example.foodieme.R
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.ui.allmenulistadapter.AllMenuListAdapter






@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    GlideApp.with(imageView.context).load(url).into(imageView)
}