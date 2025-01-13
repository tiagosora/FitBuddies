package com.example.fitbuddies.di

import android.content.Context
import android.content.SharedPreferences
import com.example.fitbuddies.data.repositories.ChallengeRepository
import com.example.fitbuddies.data.repositories.FriendshipRepository
import com.example.fitbuddies.data.repositories.UserRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Context
    @Provides
    @Singleton
    fun provideContext(application: FitBuddiesApplication): Context {
        return application.applicationContext
    }

    // SharedPreferences
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("FitBuddiesPrefs", Context.MODE_PRIVATE)
    }

    // Repositories
    @Provides
    @Singleton
    fun provideChallengeRepository(): ChallengeRepository {
        return ChallengeRepository()
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }

    // FusedLocationProviderClient
    @Provides
    @Singleton
    fun provideFusedLocationClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}
