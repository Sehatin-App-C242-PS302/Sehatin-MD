package com.c242_ps302.sehatin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity

@Dao
interface RecommendationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendation(recommendation: RecommendationEntity)

    @Query("SELECT * FROM recommendation_data")
    suspend fun getAllRecommendations(): List<RecommendationEntity>

    @Query("SELECT * FROM recommendation_data WHERE id = :recommendationId")
    suspend fun getRecommendationById(recommendationId: Int): RecommendationEntity?

    @Query("DELETE FROM recommendation_data")
    suspend fun deleteAllRecommendations()

    @Query("DELETE FROM recommendation_data WHERE id = :recommendationId")
    suspend fun deleteRecommendationById(recommendationId: Int)
}