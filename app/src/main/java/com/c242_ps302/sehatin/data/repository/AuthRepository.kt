package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.remote.AuthApiService

class AuthRepository(
    private val authApiService: AuthApiService,
    private val preferences: SehatinAppPreferences
) {
}