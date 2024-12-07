package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.remote.RecommendationApiService
import com.c242_ps302.sehatin.data.remote.response.PostRecommendationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecommendationRepository @Inject constructor(
    private val recommendationApiService: RecommendationApiService,
    private val userDao: UserDao,
) {
    fun postRecommendationAndSave(
        gender: String,
        age: Int,
        height: Double,
        weight: Double,
    ): Flow<Result<PostRecommendationResponse>> = flow {
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


}