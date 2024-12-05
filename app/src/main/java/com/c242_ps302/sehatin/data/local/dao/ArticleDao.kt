package com.c242_ps302.sehatin.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.c242_ps302.sehatin.data.local.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article_data")
    fun getAllArticles(): PagingSource<Int, ArticleEntity>

    @Upsert
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM article_data")
    suspend fun deleteAllArticles()
}