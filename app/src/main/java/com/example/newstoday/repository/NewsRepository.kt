package com.example.newstoday.repository

import com.example.newstoday.db.Dao
import com.example.newstoday.models.Article
import com.example.newstoday.network.api.NewsApiService
import javax.inject.Inject


class NewsRepository
@Inject
constructor( val newsApiService: NewsApiService, val dao : Dao) {

    suspend fun getBreakingNews(countryCode: String) =
      newsApiService.getBreakingNews(countryCode)

    suspend fun searchNews(searchQuery: String) =
        newsApiService.searchForNews(searchQuery)

    suspend fun upsert(article: Article) = dao.upsert(article)

    fun getSavedNews() = dao.getAllArticles()

    suspend fun deleteArticle(article: Article) =dao.deleteArticle(article)



}