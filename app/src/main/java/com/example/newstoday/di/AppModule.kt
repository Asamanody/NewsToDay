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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient, gsonBuilder: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NewsApiService {
        return retrofit
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
