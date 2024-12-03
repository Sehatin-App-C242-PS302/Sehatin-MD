package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.remote.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class AuthRepository(
    private val authApiService: AuthApiService,
    private val preferences: SehatinAppPreferences
) {
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(
        authApiService: AuthApiService, // Provide dependencies
        preferences: SehatinAppPreferences
    ): AuthRepository {
        return AuthRepository(authApiService, preferences)
    }
}