package com.certified.covid19response.data.network

import com.certified.covid19response.data.model.NewsApiOrgResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CovidResponseApiService {

    @GET("everything")
    suspend fun getNewsApiOrgNews(
        @Header("X-Api-Key") apiKey: String,
        @Query("q") query: String
    ): Response<NewsApiOrgResponse>

    @GET("top-headlines")
    suspend fun getNewsApiOrgHeadlines(
        @Header("X-Api-Key") apiKey: String,
        @Query("country") country: String,
        @Query("q") query: String
    ): Response<NewsApiOrgResponse>
}