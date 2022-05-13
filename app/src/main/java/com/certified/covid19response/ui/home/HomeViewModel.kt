package com.certified.covid19response.ui.home

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.BuildConfig
import com.certified.covid19response.data.model.NewsApiOrgArticle
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.data.repository.Repository
import com.certified.covid19response.util.ApiErrorUtil
import com.certified.covid19response.util.Config
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FirebaseRepository,
    private val covidRepo: Repository,
    private val apiErrorUtil: ApiErrorUtil
) :
    ViewModel() {

    val uiState = ObservableField(UIState.LOADING)

    private val _newsApiOrgNews = MutableLiveData<List<NewsApiOrgArticle>>()
    val newsApiOrgNews: LiveData<List<NewsApiOrgArticle>> get() = _newsApiOrgNews

    private val _newsApiOrgArticle = MutableLiveData<List<NewsApiOrgArticle>>()
    val newsApiOrgArticle: LiveData<List<NewsApiOrgArticle>> get() = _newsApiOrgArticle

    private val apiKey = BuildConfig.NEWS_API_ORG_API_KEY

    init {
        getNewsApiOrgNews()
        getNewsApiOrgHeadlines()
    }

    private fun getNewsApiOrgNews() {
        viewModelScope.launch {
            try {
                val response = covidRepo.getNewsApiOrgNews(apiKey, "covid")
                if (response.isSuccessful) {
                    uiState.set(UIState.SUCCESS)
                    _newsApiOrgArticle.value =
                        response.body()?.articles?.sortedByDescending { it.publishedAt }
                } else {
                    uiState.set(UIState.FAILURE)
                    Log.d("TAG", "getNewsApiOrgNews: error: ${response.body()?.message}")
                }
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                e.printStackTrace()
                Log.d("TAG", "getNewsApiOrgNews: error2: ${e.localizedMessage}")
            }
        }
    }

    private fun getNewsApiOrgHeadlines() {
        viewModelScope.launch {
            try {
                val response =
                    covidRepo.getNewsApiOrgHeadlines(apiKey, "us", "covid")
                if (response.isSuccessful) {
                    uiState.set(UIState.SUCCESS)
                    _newsApiOrgNews.value =
                        response.body()?.articles?.sortedByDescending { it.publishedAt }
                } else {
                    uiState.set(UIState.FAILURE)
                    Log.d("TAG", "getNewsApiOrgHeadlines: error: ${response.body()?.message}")
                }
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                e.printStackTrace()
                Log.d("TAG", "getNewsApiOrgHeadlines: error2: ${e.localizedMessage}")
            }
        }
    }
}