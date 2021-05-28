package com.example.foodieme.network

import android.os.Parcelable

import com.example.foodieme.database.DatabaseFlowsMenu
import com.example.foodieme.domain.FlowsMenu
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize



//@JsonClass(generateAdapter = true)
//data class NetworkFlowsMenuContainer(val flowsMenu: List<NetworkFlowsMenu>)



data class NetworkFlowsMenu(
    @SerializedName("_id")
    val _id:String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nameDetail")
    val nameDetail: String,
    @SerializedName( "rating")
    val rating: Double,
    @SerializedName("price")
    val price: Double,
    @SerializedName( "about")
    val about: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("type")
    val type: String, //to determine if the item is a popular one
    @SerializedName("image")
    val image: String,
    @SerializedName("ingre_url_one")
    val ingredients1: String,
    @SerializedName("ingre_url_two")
    val ingredients2: String,
    @SerializedName("ingre_url_three")
    val ingredients3: String,
    @SerializedName("ingre_url_four")
    val ingredients4: String,
    @SerializedName("__v")
    val __v: String,
    @SerializedName("comments")
    val comments: List<String>
    )






fun List<NetworkFlowsMenu>.asDatabaseModel() : Array<DatabaseFlowsMenu>{

    return map {
        DatabaseFlowsMenu(
            name = it.name,
            nameDetail = it.nameDetail,
            rating = it.rating,
            price = it.price,
            about =it.about,
            category = it.category,
            type = it.type,
            image = it.image,
            ingredients1 = it.ingredients1,
            ingredients2 = it.ingredients2,
            ingredients3 = it.ingredients3,
            ingredients4 = it.ingredients4
        )

    }.toTypedArray()
}






