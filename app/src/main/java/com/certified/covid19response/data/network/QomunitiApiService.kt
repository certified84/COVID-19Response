package com.certified.covid19response.data.network

import com.certified.covid19response.data.model.CatalogResponse
import retrofit2.Response
import retrofit2.http.GET

interface CovidResponseApiService {

    @GET("rds/api/catalog")
    suspend fun getCatalog(): Response<CatalogResponse>
}