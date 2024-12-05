package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.mapper.toEntity
import com.c242_ps302.sehatin.data.remote.HealthApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HealthRepository @Inject constructor(
    private val userDao: UserDao,
    private val healthApiService: HealthApiService,
    private val recommendationDao: RecommendationDao
) {
    fun getAllRecommendationByUserId(): Flow<Result<List<RecommendationEntity>>> = flow {
        emit(Result.Loading)
        try {
            val localData = recommendationDao.getAllRecommendations()
            emit(Result.Success(localData.sortedByDescending { it.createdAt }))

            try {
                val userId = userDao.getUserData().id
                val response = healthApiService.getRecommendationByUserId(userId)

                if (response.success == true) {
                    val remoteData = response.toEntity()

                    if (localData != remoteData) {
                        recommendationDao.clearAllRecommendations()
                        recommendationDao.insertAllRecommendations(remoteData)
                        emit(Result.Success(remoteData))
                    } else {
                        emit(Result.Success(localData.sortedByDescending { it.createdAt }))
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

    fun getLatestRecommendationByUserId(): Flow<Result<RecommendationEntity>> = flow {
        emit(Result.Loading)
        try {
            val localRecommendations = recommendationDao.getAllRecommendations()
            val localData = localRecommendations.maxByOrNull { it.createdAt }

            localData?.let { emit(Result.Success(it)) }

            try {
                val userId = userDao.getUserData().id
                val response = healthApiService.getRecommendationByUserId(userId)

                if (response.success == true) {
                    val remoteData = response.toEntity()
                    val latestRemoteData = remoteData.maxByOrNull { it.createdAt }

                    latestRemoteData?.let {
                        if (localData == null || localData != latestRemoteData) {
                            recommendationDao.clearAllRecommendations()
                            recommendationDao.insertAllRecommendations(remoteData)
                            emit(Result.Success(latestRemoteData))
                        }
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