package com.example.foodieme.domain

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlowsMenu (
    val name: String,
    val nameDetail: String,
    val rating: Double,
    val price: Double,
    val about: String,
    val category: String,
    val type: String,
    val image: String,
    val ingredients1: String,
    val ingredients2: String,
    val ingredients3: String,
    val ingredients4: String
) : Parcelable