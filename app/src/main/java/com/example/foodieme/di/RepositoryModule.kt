package com.example.foodieme.di

import com.example.foodieme.repository.FlowsMenuRepository
import com.example.foodieme.repository.MainMainRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {


    @Binds
     fun provideFlowsMenuRepositoryImpl(repository: FlowsMenuRepository): MainMainRepository


}