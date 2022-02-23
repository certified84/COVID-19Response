package com.certified.covid19response.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.certified.covid19response.util.UIState

class SignupViewModel : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

}