package com.example.newsapp.NewsApp.api

import com.example.newsapp.NewsApp.api.model.ApiConstants
import com.example.newsapp.NewsApp.api.model.newsResponse.NewsResponse
import com.example.newsapp.NewsApp.api.model.sourcesResponse.SourceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("/v2/top-headlines/sources")
    fun getSources(
        @Query("apiKey")Key:String = ApiConstants.apiKey
    ): Call<SourceResponse>

    @GET("/v2/everything")
    fun getNews(
        @Query("apiKey")Key: String = ApiConstants.apiKey,
        @Query("sources")Sources: String
    ): Call<NewsResponse>
}