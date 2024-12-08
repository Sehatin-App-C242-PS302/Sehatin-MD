package com.c242_ps302.sehatin.data.remote

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PUT

interface UserApiService {
    @FormUrlEncoded
    @PUT("profile")
    suspend fun updateProfile(
        @Field("name") name: String,
        @Field("email") email: String,
    )
}