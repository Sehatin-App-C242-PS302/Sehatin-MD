package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.mapper.toEntity
import com.c242_ps302.sehatin.data.remote.RecommendationApiService
import com.c242_ps302.sehatin.data.remote.response.RecommendationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecommendationRepository @Inject constructor(
    private val recommendationDao: RecommendationDao,
    private val recommendationApiService: RecommendationApiService,
    private val userDao: UserDao,
) {
    fun postRecommendationAndSave(
        gender: String,
        age: Int,
        height: Double,
        weight: Double,
    ): Flow<Result<RecommendationResponse>> = flow {
        emit(Result.Loading)
        try {
            val userId = userDao.getUserData().id

            val response = recommendationApiService.postRecommendation(
                userId = userId,
                gender = gender,
                age = age,
                height = height,
                weight = weight
            )
            if (response.success == true) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message ?: "An error occurred"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }

    fun getRecommendationByUserId(): Flow<Result<List<RecommendationEntity>>> = flow {
        emit(Result.Loading)
        try {
            val localData = recommendationDao.getAllRecommendations()
            emit(Result.Success(localData.sortedByDescending { it.createdAt }))

            try {
                val userId = userDao.getUserData().id
                val response = recommendationApiService.getRecommendationByUserId(userId)

                if (response.success == true) {
                    val remoteData = response.toEntity()

                    if (localData != remoteData) {
                        recommendationDao.clearAllRecommendations()
                        recommendationDao.insertAllRecommendations(remoteData)
                        emit(Result.Success(remoteData))
                    } else {
                        emit(Result.Success(localData.sortedByDescending { it.createdAt })) // Emit local data if no changes
                    }
                } else {
                    emit(Result.Error("Failed to fetch recommendations"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "An error occurred while fetching remote data"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred while fetching local data"))
        }
    }
}