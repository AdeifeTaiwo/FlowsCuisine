package com.example.foodieme.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodieme.domain.FlowsMenu
import com.squareup.moshi.Json

@Entity
data class DatabaseFlowsMenu constructor(
    @PrimaryKey
    val name: String,
    val nameDetail: String,
    val rating: Double,
    val price: Double,
    val about: String,
    val category: String,
    val type: String, //to determine if the item is a popular one
    val image: String,
    val ingredients1: String,
    val ingredients2: String,
    val ingredients3: String,
    val ingredients4: String
)

fun List<DatabaseFlowsMenu>.asDomainModel() : List<FlowsMenu>{
    return map {
        FlowsMenu(
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
    }
}

