package com.example.newstoday.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.newstoday.models.Article

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}