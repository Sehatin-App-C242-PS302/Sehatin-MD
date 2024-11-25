package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.mapper.toEntity
import com.c242_ps302.sehatin.data.remote.RecommendationApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecommendationRepository @Inject constructor(
    private val recommendationDao: RecommendationDao,
    private val recommendationApiService: RecommendationApiService,
) {
    fun getRecommendationFromApiAndSave(
        gender: String,
        age: Int,
        height: Double,
        weight: Double
    ): Flow<Result<RecommendationEntity>> = flow {
        emit(Result.Loading)
        try {
            val response = recommendationApiService.getRecommendation(
                gender = gender,
                age = age,
                height = height,
                weight = weight
            )

            val recommendationEntity = response.toEntity()

            recommendationDao.insertRecommendation(recommendationEntity)

            emit(Result.Success(recommendationEntity))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }
}