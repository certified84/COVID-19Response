package com.certified.covid19response.data.network

import com.certified.covid19response.data.model.CatalogResponse
import com.certified.covid19response.data.model.RapidApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CovidResponseApiService {

    @GET("rds/api/catalog")
    suspend fun getCatalog(): Response<CatalogResponse>

    @GET("news/v1/US/")
    suspend fun getNews(@Header("x-rapidapi-key") apiKey: String): Response<RapidApiResponse>
}