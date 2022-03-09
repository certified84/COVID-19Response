package com.certified.covid19response.ui.home

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.model.Article
import com.certified.covid19response.data.model.DataProduct
import com.certified.covid19response.data.model.News
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.data.repository.Repository
import com.certified.covid19response.util.ApiErrorUtil
import com.certified.covid19response.util.Config
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _latestNews = MutableLiveData<List<News>>()
    val latestNews: LiveData<List<News>> get() = _latestNews

    private val _latestArticles = MutableLiveData<List<Article>>()
    val latestArticles: LiveData<List<Article>> get() = _latestArticles

    private val _data = MutableLiveData<List<DataProduct>>()
    val data: LiveData<List<DataProduct>> get() = _data

    init {
        getNews()
    }

    fun getCatalog() {
        viewModelScope.launch {
            try {
                Log.d("TAG", "getData: Init")
                val response = covidRepo.getCatalog()
                if (response.isSuccessful) {
                    Log.d("TAG", "getCatalog: ${response.body()}")
                    _data.value = response.body()?.catalogs?.get(0)?.dataProducts
                } else {
                    uiState.set(UIState.FAILURE)
                    val error = apiErrorUtil.parseError(response)
                    Log.d("TAG", "getData: error: ${error?.error}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", "getCatalog: error2: ${e.localizedMessage}")
            }
        }
    }

    private fun getNews() {
        viewModelScope.launch {
            try {
                Log.d("TAG", "getNews: Init")
                val response = covidRepo.getNews(Config.RAPID_API_KEY)
                if (response.isSuccessful) {
                    uiState.set(UIState.SUCCESS)
                    Log.d("TAG", "getNews: ${response.body()}")
                    _latestNews.value = response.body()?.news
                    Log.d("TAG", "getNews: Image: ${response.body()?.news?.get(0)?.images?.get(0)}")
                } else {
                    uiState.set(UIState.FAILURE)
                    Log.d("TAG", "getNews: error: ${response.message()}")
                }
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                e.printStackTrace()
                Log.d("TAG", "getCatalog: error2: ${e.localizedMessage}")
            }
        }
    }
}