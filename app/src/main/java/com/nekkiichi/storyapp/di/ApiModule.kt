package com.nekkiichi.storyapp.di

import com.nekkiichi.storyapp.data.remote.services.ApiConfig
import com.nekkiichi.storyapp.data.remote.services.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun provideApiService(): ApiService = ApiConfig.getApiService()
}