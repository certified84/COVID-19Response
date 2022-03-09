package com.certified.covid19response.ui.result

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel() {

    val uiState = ObservableField(UIState.LOADING)
}