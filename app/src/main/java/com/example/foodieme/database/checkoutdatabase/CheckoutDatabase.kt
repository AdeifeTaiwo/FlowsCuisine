package com.example.foodieme.database.checkoutdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Checkout::class], version = 1, exportSchema = false)

abstract class CheckoutDatabase: RoomDatabase() {
    /**
     * Connects the database to the DAP
     */

    abstract val checkoutDatabaseDao: CheckoutDatabaseDao

}