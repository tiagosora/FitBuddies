package com.example.fitbuddies.di

import android.content.Context
import android.content.SharedPreferences
import com.example.fitbuddies.data.repositories.UserRepository
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
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }

}
