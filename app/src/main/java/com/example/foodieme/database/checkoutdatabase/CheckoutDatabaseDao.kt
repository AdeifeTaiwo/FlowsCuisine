package com.example.foodieme.database.checkoutdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodieme.domain.CheckoutMenu


@Dao
interface CheckoutDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(night: Checkout)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param night new value to write
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(night: Checkout)

    /**
     * get all checkoout page data from the page
     */
    @Query("SELECT * FROM check_out_page_table ORDER BY checkOutId DESC")
    fun getAllNights(): LiveData<List<Checkout>>

    @Query("SELECT * FROM check_out_page_table ORDER BY checkOutId DESC")
    fun getAllNightsNonLivedata(): List<Checkout>


    /**
     * Selects and returns the checkout data with given nightId.
     */
    @Query("SELECT * from check_out_page_table WHERE checkOutId = :key")
    fun getNightWithId(key: Long): Checkout

    @Query("SELECT * FROM check_out_page_table ORDER BY checkOutId DESC LIMIT 1")
    suspend fun getLatestAddition(): Checkout?

    @Query("DELETE FROM check_out_page_table")
    suspend fun clear()

    @Query("DELETE FROM check_out_page_table WHERE checkOutId = :key")
    suspend fun clearWithId(key: Long)
}