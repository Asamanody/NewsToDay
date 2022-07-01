package com.example.newstoday.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstoday.NewsApplication
import com.example.newstoday.models.Article
import com.example.newstoday.models.NewsResponse
import com.example.newstoday.repository.NewsRepository
import com.example.newstoday.util.Resource
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject
constructor(private val newsRepository: NewsRepository)
    : ViewModel() {

     val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
     var brekingNewsPage = 1
      var text1= "hello"

      val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
       var searchNewsPage = 1
    init {
        getBreakingNews("us")
    }


    fun getBreakingNews(countryCode: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = newsRepository.getBreakingNews(countryCode)
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNews.postValue(Resource.Success(resultResponse))
            }
        }
        else
            breakingNews.postValue(Resource.Error(response.message()))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = newsRepository.searchNews(searchQuery)
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNews.postValue(Resource.Success(resultResponse))
            }
        }
        else
            breakingNews.postValue(Resource.Error(response.message()))
    }


    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }






}