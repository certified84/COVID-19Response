package com.certified.covid19response.ui.chat

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)
}