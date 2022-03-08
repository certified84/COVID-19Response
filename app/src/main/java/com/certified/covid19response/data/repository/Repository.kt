package com.certified.covid19response.data.repository

import com.certified.covid19response.data.network.CovidResponseApiService
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: CovidResponseApiService) {

    suspend fun getCatalog() = apiService.getCatalog()

    suspend fun getNews(apiKey: String) = apiService.getNews(apiKey)
}