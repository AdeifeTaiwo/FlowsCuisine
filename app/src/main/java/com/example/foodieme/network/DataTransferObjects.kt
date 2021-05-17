package com.example.foodieme.network

import android.os.Parcelable

import com.example.foodieme.database.DatabaseFlowsMenu
import com.example.foodieme.domain.FlowsMenu
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize



//@JsonClass(generateAdapter = true)
//data class NetworkFlowsMenuContainer(val flowsMenu: List<NetworkFlowsMenu>)


@JsonClass(generateAdapter = true)
data class NetworkFlowsMenu(
    @Json(name = "name")
    val name: String,
    @Json(name = "nameDetail")
    val nameDetail: String,
    @Json(name = "rating")
    val rating: Double,
    @Json(name = "price")
    val price: Double,
    @Json(name = "about")
    val about: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "type")
    val type: String, //to determine if the item is a popular one
    @Json(name = "image")
    val image: String,
    @Json(name = "ingre_url_one")
    val ingredients1: String,
    @Json(name = "ingre_url_two")
    val ingredients2: String,
    @Json(name = "ingre_url_three")
    val ingredients3: String,
    @Json(name = "ingre_url_four")
    val ingredients4: String
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


