package com.certified.covid19response.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

//    @Singleton
//    @Provides
//    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .baseUrl(BASE_URL)
//        .build()

//    @Provides
//    fun provideGson(): Gson = GsonBuilder()
//        .setLenient()
//        .create()

//    @Provides
//    fun provideCovidResponseApiService(retrofit: Retrofit): CovidResponseApiService =
//        retrofit.create(CovidResponseApiService::class.java)
//
//    @Provides
//    fun provideRepository(qomunitiApiService: CovidResponseApiService) = Repository(qomunitiApiService)
//
//    @Provides
//    fun provideErrorUtils(retrofit: Retrofit) = ApiErrorUtil(retrofit)
}