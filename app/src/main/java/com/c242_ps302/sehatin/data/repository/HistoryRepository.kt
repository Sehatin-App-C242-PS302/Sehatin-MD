package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val recommendationDao: RecommendationDao
) {
    fun getAllRecommendation(): Flow<Result<List<RecommendationEntity>>> = flow {
        emit(Result.Loading)
        try {
            val recommendationEntity = recommendationDao.getAllRecommendations()
            emit(Result.Success(recommendationEntity))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }
    
}