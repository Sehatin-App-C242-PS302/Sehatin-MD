package com.c242_ps302.sehatin.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, RecommendationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SehatinDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recommendationDao(): RecommendationDao
}