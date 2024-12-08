package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.entity.UserEntity
import com.c242_ps302.sehatin.data.mapper.toEntity
import com.c242_ps302.sehatin.data.remote.UserApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val userDao: UserDao
) {
    fun updateProfile(name: String, email: String): Flow<Result<UserEntity>> = flow {
        emit(Result.Loading)
        try {
            val response = userApiService.updateProfile(name, email)
            if (response.success == true) {
                val user = response.data?.toEntity()
                if (user != null) {
                    userDao.clearUserData()
                    userDao.insertUser(user)
                    emit(Result.Success(user))
                } else {
                    emit(Result.Error("Failed to update profile"))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }

    fun getUserData(): Flow<Result<UserEntity>> = flow {
        emit(Result.Loading)
        try {
            val user = userDao.getUserData()
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }
}