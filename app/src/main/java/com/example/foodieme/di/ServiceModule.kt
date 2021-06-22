package com.example.foodieme.di

import com.example.foodieme.network.FlowsMenuService

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ServiceModule {




    @Provides
    @Singleton
    fun provideApiService(): FlowsMenuService {
        return Retrofit.Builder()

            .baseUrl("http://stark-headland-26585.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(FlowsMenuService::class.java)

    }
}