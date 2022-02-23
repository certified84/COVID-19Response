package com.certified.covid19response.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ActivityRetainedComponent::class)
@Module
class AppModule {

    @Provides
    fun provideAppContext(@ApplicationContext context: Application): Context {
        return context.applicationContext
    }
}