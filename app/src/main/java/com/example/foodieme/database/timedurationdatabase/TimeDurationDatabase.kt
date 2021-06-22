package com.example.foodieme.database.timedurationdatabase

import androidx.room.Database
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TimeAndDuration::class], version = 1, exportSchema = false)


abstract class TimeDurationDatabase: RoomDatabase() {
    /**
     * Connects the database to the DAP
     */
    abstract val durationDatabaseDao: TimeDurationDao

}