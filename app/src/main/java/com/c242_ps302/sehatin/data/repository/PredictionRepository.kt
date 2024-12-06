package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.remote.PredictionApiService
import com.c242_ps302.sehatin.data.remote.response.PostPredictionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class PredictionRepository @Inject constructor(
    private val predictionApiService: PredictionApiService,
) {
    fun postPrediction(
        foodImage: MultipartBody.Part,
    ): Flow<Result<PostPredictionResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = predictionApiService.postPrediction(foodImage)
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