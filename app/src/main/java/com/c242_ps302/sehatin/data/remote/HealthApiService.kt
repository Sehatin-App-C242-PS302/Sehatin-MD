package com.c242_ps302.sehatin.data.remote

import retrofit2.http.GET

interface HealthApiService {
    @GET("health/current")
    suspend fun getCurrentHealthData()

    @GET("health")
    suspend fun getAllHealthData()
}