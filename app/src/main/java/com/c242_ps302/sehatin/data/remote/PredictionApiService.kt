package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.PostPredictionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PredictionApiService {
    @Multipart
    @POST("predict/image")
    suspend fun postPrediction(
        @Part file: MultipartBody.Part
    ) : PostPredictionResponse
}