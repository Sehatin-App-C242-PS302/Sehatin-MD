package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.remote.AuthApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val userDao: UserDao,
    private val preferences: SehatinAppPreferences
) {
}