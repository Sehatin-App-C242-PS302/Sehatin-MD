package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.remote.UserApiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val userDao: UserDao
) {
}