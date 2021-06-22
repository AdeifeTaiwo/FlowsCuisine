package com.example.foodieme.di

import android.content.Context
import androidx.room.Room
import com.example.foodieme.database.FlowsMenuDao
import com.example.foodieme.database.FlowsMenuDatabase
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao
import com.example.foodieme.database.timedurationdatabase.TimeDurationDao
import com.example.foodieme.database.timedurationdatabase.TimeDurationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCheckoutDatabase(@ApplicationContext appContext: Context): CheckoutDatabase {
        return Room.databaseBuilder(
            appContext,
            CheckoutDatabase::class.java,
            "sleep_history_database"
        ).build()
    }


    @Provides
    fun provideCheckoutDao(database: CheckoutDatabase): CheckoutDatabaseDao {
        return database.checkoutDatabaseDao
    }






    @Provides
    @Singleton
    fun provideFlowsMenuDatabase(@ApplicationContext appContext: Context) : FlowsMenuDatabase{
        return Room.databaseBuilder(
            appContext,
            FlowsMenuDatabase::class.java,
            "videos"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideFlowsMenuDao(database: FlowsMenuDatabase): FlowsMenuDao {
        return database.flowsMenuDao
    }




    @Provides
    @Singleton
    fun provideTimeDurationDatabase(@ApplicationContext appContext: Context): TimeDurationDatabase {
        return Room.databaseBuilder(
            appContext,
            TimeDurationDatabase::class.java,
            "time_database"
        )
            // Wipes and rebuilds instead of migrating if no Migration object.
            // Migration is not part of this lesson. You can learn more about
            // migration with Room in this blog post:
            // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTimeDurationDao(database: TimeDurationDatabase): TimeDurationDao {
        return database.durationDatabaseDao
    }


}