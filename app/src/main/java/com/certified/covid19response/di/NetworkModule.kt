package com.certified.covid19response.di

import com.certified.covid19response.data.network.CovidResponseApiService
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.data.repository.Repository
import com.certified.covid19response.util.ApiErrorUtil
import com.certified.covid19response.util.Config.BASE_URL
import com.certified.covid19response.util.Config.BASE_URL_OTHER
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideFirebase(): Firebase = Firebase

    @Provides
    fun provideFirebaseRepository(): FirebaseRepository = FirebaseRepository()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL_OTHER)
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    fun provideCovidResponseApiService(retrofit: Retrofit): CovidResponseApiService =
        retrofit.create(CovidResponseApiService::class.java)

    @Provides
    fun provideRepository(apiService: CovidResponseApiService) =
        Repository(apiService)

    @Provides
    fun provideErrorUtils(retrofit: Retrofit) = ApiErrorUtil(retrofit)
}