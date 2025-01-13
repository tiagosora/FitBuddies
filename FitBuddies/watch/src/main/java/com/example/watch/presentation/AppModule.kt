package com.example.pulsewearos.presentation

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHealthServicesManager(
        @ApplicationContext context: Context
    ): HealthServicesManager {
        return HealthServicesManager(context)
    }
}
