package com.certified.covid19response.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.certified.covid19response.data.model.Article
import com.certified.covid19response.data.model.News
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    private val _latestNews = MutableLiveData<List<News>>()
    val latestNews: LiveData<List<News>> get() = _latestNews

    private val _latestArticles = MutableLiveData<List<Article>>()
    val latestArticles: LiveData<List<Article>> get() = _latestArticles
}