package com.c242_ps302.sehatin.data.repository

import android.content.Context
import android.util.Log
import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.mapper.toEntity
import com.c242_ps302.sehatin.data.remote.HealthApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HealthRepository @Inject constructor(
    private val userDao: UserDao,
    private val healthApiService: HealthApiService,
    private val recommendationDao: RecommendationDao,
    private val context: Context
) {
    fun getAllRecommendationByUserId(): Flow<Result<List<RecommendationEntity>>> = flow {
        emit(Result.Loading)
        try {
            val localData = recommendationDao.getAllRecommendations()

            val user = userDao.getUserData()
            val userId = user.id
            val response = healthApiService.getRecommendationByUserId(userId)

            if (response.success == true) {
                val remoteData = response.toEntity(context)

                if (localData != remoteData) {
                    recommendationDao.clearAllRecommendations()
                    recommendationDao.insertAllRecommendations(remoteData)
                    emit(Result.Success(remoteData.sortedByDescending { it.createdAt }))
                } else {
                    emit(Result.Success(localData.sortedByDescending { it.createdAt }))
                }
            } else {
                emit(Result.Success(localData.sortedByDescending { it.createdAt }))
            }
        } catch (e: Exception) {
            val localData = recommendationDao.getAllRecommendations()
            emit(
                if (localData.isNotEmpty()) {
                    Result.Success(localData.sortedByDescending { it.createdAt })
                } else {
                    Result.Error(e.message ?: "An error occurred")
                }
            )
        }
    }

    fun getLatestRecommendationByUserId(): Flow<Result<RecommendationEntity>> = flow {
        emit(Result.Loading)
        try {
            val localRecommendations = recommendationDao.getAllRecommendations()
            val localData = localRecommendations.maxByOrNull { it.createdAt }

            localData?.let { emit(Result.Success(it)) }

            val user = userDao.getUserData()
            val userId = user.id
            val response = healthApiService.getRecommendationByUserId(userId)

            if (response.success == true && response.data?.isNotEmpty() == true) {
                val remoteData = response.toEntity(context)
                val latestRemoteData = remoteData.maxByOrNull { it.createdAt }

                latestRemoteData?.let {
                    if (localData == null || localData != latestRemoteData) {
                        recommendationDao.clearAllRecommendations()
                        recommendationDao.insertAllRecommendations(remoteData)
                        emit(Result.Success(latestRemoteData))
                    }
                }
            } else {
                if (localData == null) {
                    emit(Result.Error("No health data available"))
                }
            }
        } catch (e: Exception) {
            val localRecommendations = recommendationDao.getAllRecommendations()
            val localData = localRecommendations.maxByOrNull { it.createdAt }

            localData?.let {
                emit(Result.Success(it))
            } ?: emit(Result.Error(e.message ?: "An error occurred"))
        }
    }


    suspend fun getLastHealthData(): RecommendationEntity? {
        return withContext(Dispatchers.IO) {
            try {
                recommendationDao.getCurrentRecommendation()
            } catch (e: Exception) {
                Log.d("NoteRepository", "Database error: ${e.message}")
                null
            }
        }
    }
}