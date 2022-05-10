package com.example.newstoday.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newstoday.models.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false

)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun dao() : Dao

}