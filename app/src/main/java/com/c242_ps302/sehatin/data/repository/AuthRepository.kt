package com.c242_ps302.sehatin.data.repository

import android.content.Context
import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.entity.UserEntity
import com.c242_ps302.sehatin.data.mapper.toEntity
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.remote.AuthApiService
import com.c242_ps302.sehatin.data.remote.response.LoginResponse
import com.c242_ps302.sehatin.data.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val context: Context,
    private val authApiService: AuthApiService,
    private val userDao: UserDao,
    private val preferences: SehatinAppPreferences,
) {
    fun login(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = authApiService.login(email, password)
            if (response.success == true) {
                response.token?.let { token ->
                    withContext(Dispatchers.IO) {
                        preferences.setToken(token)
                        preferences.getToken().first().also { savedToken ->
                            if (savedToken.isEmpty()) {
                                throw Exception("Token tidak tersimpan")
                            }
                        }
                    }
                }
                userDao.clearUserData()
                response.user?.let { user ->
                    userDao.insertUser (user.toEntity())
                    emit(Result.Success(response))
                }
            } else {
                emit(Result.Error((response.message.toString())))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String): Flow<Result<RegisterResponse>> =
        flow {
            emit(Result.Loading)
            try {
                val response = authApiService.register(name, email, password)
                if (response.success == true) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error((response.message.toString())))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    suspend fun logout() = runCatching {
        withContext(Dispatchers.IO) {
            preferences.setToken("")
            userDao.clearUserData()
        }
    }.fold(
        onSuccess = { Result.Success(Unit) },
        onFailure = { Result.Error(it.message.toString()) }
    )

    fun getToken(): Flow<String> {
        return preferences.getToken()
    }

    fun getUser(): Flow<Result<UserEntity>> = flow {
        emit(Result.Loading)
        try {
            val user = userDao.getUserData()
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}