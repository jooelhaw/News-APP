package com.example.newsapp.NewsApp.ui.News

import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.NewsApp.api.model.ApiManager
import com.example.newsapp.NewsApp.api.model.newsResponse.News
import com.example.newsapp.NewsApp.api.model.newsResponse.NewsResponse
import com.example.newsapp.NewsApp.api.model.sourcesResponse.Source
import com.example.newsapp.NewsApp.api.model.sourcesResponse.SourceResponse
import com.example.newsapp.NewsApp.ui.ViewError
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(): ViewModel() {
    val shouldShowLoading = MutableLiveData<Boolean>()
    val sourceList = MutableLiveData<List<Source?>?>()
    val newsList = MutableLiveData<List<News?>?>()
    val errorLiveData = MutableLiveData<ViewError>()

    fun getNewsSources(category: String) {
        shouldShowLoading.postValue(true)
        ApiManager.getApis().getSources(category = category).enqueue(object :
            Callback<SourceResponse> {
            override fun onFailure(call: Call<SourceResponse>, t: Throwable) {
                shouldShowLoading.postValue(false)
                errorLiveData.postValue(
                    ViewError(
                        throwable = t
                    ){
                        getNewsSources(category)
                    }
                )
            }

            override fun onResponse(call: Call<SourceResponse>, response: Response<SourceResponse>) {
                shouldShowLoading.postValue(false)
                if (response.isSuccessful){
                    sourceList.postValue(response.body()?.sources)
                }else{
                    val errorBodyJsonString = response.errorBody()?.string()
                    val response = Gson().fromJson(errorBodyJsonString, SourceResponse::class.java)
                    errorLiveData.postValue(
                        ViewError(
                            message = response.message
                        ){
                            getNewsSources(category)
                        }
                    )
                }

            }
        })
    }
    fun getNews(id: String?) {
        shouldShowLoading.postValue(true)
        ApiManager.getApis().getNews(Sources = id?:"").enqueue(object : Callback<NewsResponse>{
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                shouldShowLoading.postValue(false)
                errorLiveData.postValue(
                    ViewError(
                        throwable = t
                    ){
                        getNews(id)
                    }
                )
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                shouldShowLoading.postValue(false)
                if (response.isSuccessful){
                    newsList.postValue(response.body()?.articles)
                    return
                }
                val responseJsonError = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(responseJsonError, NewsResponse::class.java)
                errorLiveData.postValue(
                    ViewError(
                        message = errorResponse.message
                    ){
                        getNews(id)
                    }
                )
            }
        })
    }
}