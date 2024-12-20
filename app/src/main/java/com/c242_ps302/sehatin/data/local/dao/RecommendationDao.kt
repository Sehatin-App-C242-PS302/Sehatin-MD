package com.c242_ps302.sehatin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity

@Dao
interface RecommendationDao {
    @Query("SELECT * FROM recommendation_data")
    suspend fun getAllRecommendations(): List<RecommendationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRecommendations(recommendations: List<RecommendationEntity>)

    @Update
    suspend fun updateRecommendation(recommendation: RecommendationEntity)

    @Query("SELECT * FROM recommendation_data ORDER BY createdAt DESC LIMIT 1")
    suspend fun getCurrentRecommendation(): RecommendationEntity

    @Query("DELETE FROM recommendation_data")
    suspend fun clearAllRecommendations()
}