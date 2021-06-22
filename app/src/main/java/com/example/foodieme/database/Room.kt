package com.example.foodieme.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FlowsMenuDao {
    @Query("select * from DatabaseFlowsMenu")
    fun getFlowsMenu(): LiveData<List<DatabaseFlowsMenu>>


    //get List by Popular
    @Query("select * from DatabaseFlowsMenu where type=:key")
    fun getPopularFlowsMenu(key: String): LiveData<List<DatabaseFlowsMenu>>

    @Query("select * from DatabaseFlowsMenu where category=:key")
    fun getSnackFlowsMenu(key: String): LiveData<List<DatabaseFlowsMenu>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg videos: DatabaseFlowsMenu)
}




@Database(entities = [DatabaseFlowsMenu::class], version = 1, exportSchema = false)

abstract class FlowsMenuDatabase : RoomDatabase() {
    abstract val flowsMenuDao: FlowsMenuDao
}


