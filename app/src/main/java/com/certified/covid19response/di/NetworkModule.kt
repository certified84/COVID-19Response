package com.certified.covid19response.di

import com.certified.covid19response.data.repository.FirebaseRepository
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
    fun provideFirebase(): Firebase = Firebase

    @Provides
    fun provideFirebaseRepository(): FirebaseRepository = FirebaseRepository()

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
//    fun provideRepository(qomunitiApiService: CovidResponseApiService) = FirebaseRepository(qomunitiApiService)
//
//    @Provides
//    fun provideErrorUtils(retrofit: Retrofit) = ApiErrorUtil(retrofit)
}