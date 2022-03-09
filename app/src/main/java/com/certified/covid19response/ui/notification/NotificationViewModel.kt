package com.certified.covid19response.ui.notification

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)
}