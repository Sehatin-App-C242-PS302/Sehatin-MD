package com.c242_ps302.sehatin.data.repository

import android.content.Context
import com.c242_ps302.sehatin.data.local.dao.PredictionDao
import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.entity.PredictionEntity
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.mapper.toEntity
import com.c242_ps302.sehatin.data.remote.HealthApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HealthRepository @Inject constructor(
    private val userDao: UserDao,
    private val healthApiService: HealthApiService,
    private val recommendationDao: RecommendationDao,
    private val predictionDao: PredictionDao,
    private val context: Context
) {
    fun getAllRecommendationsByUserId(): Flow<Result<List<RecommendationEntity>>> = flow {
        emit(Result.Loading)
        try {
            val user = userDao.getUserData()
            val userId = user.id

            val response = healthApiService.getRecommendationByUserId(userId)

            val localData = recommendationDao.getAllRecommendations()

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
            val user = userDao.getUserData()
            val userId = user.id

            val response = healthApiService.getRecommendationByUserId(userId)

            if (response.success == true && response.data?.isNotEmpty() == true) {
                val remoteData = response.toEntity(context)
                val latestRemoteData = remoteData.maxByOrNull { it.createdAt }

                val localRecommendations = recommendationDao.getAllRecommendations()
                val localData = localRecommendations.maxByOrNull { it.createdAt }

                latestRemoteData?.let {
                    if (localData == null || localData != latestRemoteData) {
                        recommendationDao.clearAllRecommendations()
                        recommendationDao.insertAllRecommendations(remoteData)
                        emit(Result.Success(latestRemoteData))
                    } else {
                        emit(Result.Success(localData))
                    }
                } ?: emit(Result.Error("No health data available"))
            } else {
                val localRecommendations = recommendationDao.getAllRecommendations()
                val localData = localRecommendations.maxByOrNull { it.createdAt }

                localData?.let {
                    emit(Result.Success(it))
                } ?: emit(Result.Error("No health data available"))
            }
        } catch (e: Exception) {
            val localRecommendations = recommendationDao.getAllRecommendations()
            val localData = localRecommendations.maxByOrNull { it.createdAt }

            localData?.let {
                emit(Result.Success(it))
            } ?: emit(Result.Error(e.message ?: "An error occurred"))
        }
    }

    fun getAllPredictions(): Flow<Result<List<PredictionEntity>>> = flow {
        emit(Result.Loading)
        try {
            val localData = predictionDao.getAllPredictions()
            val response = healthApiService.getPredictionByUser()

            if (response.success == true) {
                val remoteData = response.toEntity()

                if (localData != remoteData) {
                    predictionDao.clearAllPredictions()
                    predictionDao.insertAllPredictions(remoteData)
                    emit(Result.Success(remoteData.sortedByDescending { it.createdAt }))
                } else {
                    emit(Result.Success(localData.sortedByDescending { it.createdAt }))
                }
            } else {
                emit(Result.Success(localData.sortedByDescending { it.createdAt }))
            }
        } catch (e: Exception) {
            val localData = predictionDao.getAllPredictions()
            emit(
                if (localData.isNotEmpty()) {
                    Result.Success(localData.sortedByDescending { it.createdAt })
                } else {
                    Result.Error(e.message ?: "An error occurred")
                }
            )
        }
    }

    fun getLatestPrediction(): Flow<Result<PredictionEntity>> = flow {
        emit(Result.Loading)
        try {
            val localPredictions = predictionDao.getAllPredictions()
            val localData = localPredictions.maxByOrNull { it.createdAt }

            localData?.let { emit(Result.Success(it)) }

            val response = healthApiService.getPredictionByUser()

            if (response.success == true && response.data?.isNotEmpty() == true) {
                val remoteData = response.toEntity()
                val latestRemoteData = remoteData.maxByOrNull { it.createdAt }

                latestRemoteData?.let {
                    if (localData == null || localData != latestRemoteData) {
                        predictionDao.clearAllPredictions()
                        predictionDao.insertAllPredictions(remoteData)
                        emit(Result.Success(latestRemoteData))
                    }
                }
            } else {
                if (localData == null) {
                    emit(Result.Error("No prediction data available"))
                }
            }
        } catch (e: Exception) {
            val localPredictions = predictionDao.getAllPredictions()
            val localData = localPredictions.maxByOrNull { it.createdAt }

            localData?.let {
                emit(Result.Success(it))
            } ?: emit(Result.Error(e.message ?: "An error occurred"))
        }
    }
}