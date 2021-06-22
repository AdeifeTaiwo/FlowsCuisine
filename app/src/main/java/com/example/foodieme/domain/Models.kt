package com.example.foodieme.domain

import android.os.Parcelable
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


@Parcelize
data class CheckoutMenu(
    val checkoutID: Long,
    val imageUrl: String,
    val priceInfo: Double,
    val weight: String,
    val quantity: Int,
    val name: String,
    val duration: Long,
    val isActive: Boolean
) : Parcelable


@Parcelize
data class TimeDistance(
    val id: Long,
    val duration: Long,
    val distance: String
): Parcelable