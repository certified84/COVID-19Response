package com.certified.covid19response.data.repository

import com.certified.covid19response.data.network.CovidResponseApiService
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: CovidResponseApiService) {

    suspend fun getNewsApiOrgNews(apiKey: String, query: String) =
        apiService.getNewsApiOrgNews(apiKey, query)

    suspend fun getNewsApiOrgHeadlines(apiKey: String, country: String, query: String) =
        apiService.getNewsApiOrgHeadlines(apiKey, country, query)
}