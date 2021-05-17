package com.example.foodieme.database.checkoutdatabase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.foodieme.domain.CheckoutMenu

@Entity( indices = [Index(value = ["image_url", "name"], unique = true)],  tableName = "check_out_page_table" )


data class Checkout(
    @PrimaryKey(autoGenerate = true)
    var checkOutId: Long = 0L,

    @ColumnInfo(name = "image_url")
    var imageUrl: String = "",

    @ColumnInfo(name = "priceInfo")
    var priceInfo: Double = 0.0,


    @ColumnInfo(name = "weight")
    var weight: String ="2",

    @ColumnInfo(name = "quantity")
    var quantity: Int = 0,

     @ColumnInfo(name = "name")
    var name: String = "th"
)

fun List<Checkout>.asDomainModel(): List<CheckoutMenu>{
    return map{
        CheckoutMenu(
            checkoutID = it.checkOutId,
            imageUrl = it.imageUrl,
            priceInfo = it.priceInfo,
            weight = it.weight,
            quantity = it.quantity,
            name = it.name

        )
    }
}


