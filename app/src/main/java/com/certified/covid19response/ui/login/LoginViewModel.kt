package com.certified.covid19response.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private var _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                repository.signInWithEmailAndPassword(email, password).await()
                uiState.set(UIState.SUCCESS)
                _success.value = true
                _message.value = "Welcome $email"
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                e.printStackTrace()
                _success.value = false
                _message.value = "Authentication failed: ${e.localizedMessage}"
            }
        }
    }
}