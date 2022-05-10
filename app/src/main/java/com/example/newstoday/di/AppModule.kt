package com.example.newstoday.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newstoday.db.ArticleDatabase
import com.example.newstoday.network.api.NewsApiService
import com.example.newstoday.repository.NewsRepository
import com.example.newstoday.util.Constants.Companion.API_KEY
import com.example.newstoday.util.Constants.Companion.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gsonBuilder: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
    }

    @Singleton
    @Provides
    fun provideApiService(retrofitBuilder: Retrofit.Builder): NewsApiService {
        return retrofitBuilder
            .build()
            .create(NewsApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideDatabaseBuilder(
        @ApplicationContext context: Context
    ): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "article_db.db"
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideDatabaseDao(
        db: ArticleDatabase
    ): com.example.newstoday.db.Dao {
        return db.dao()


    }

    @Singleton
    @Provides
    fun provideRepository(newsApiService: NewsApiService, dao : com.example.newstoday.db.Dao) : NewsRepository{

        return NewsRepository(newsApiService,dao)

    }
}
