package com.example.foodieme.database.timedurationdatabase

import androidx.room.*
import com.example.foodieme.database.checkoutdatabase.Checkout


@Dao
interface TimeDurationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(night: TimeAndDuration)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(night: TimeAndDuration)

    @Query("SELECT * FROM time_duration_database ORDER BY id DESC LIMIT 1")
    suspend fun getLatestAddition(): TimeAndDuration?

    @Query("SELECT * from time_duration_database WHERE id = :key")
    fun retrieveWithId(key: Long): TimeAndDuration

}