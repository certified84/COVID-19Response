package com.certified.covid19response.ui.passwordRecovery

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.Extensions.showToast
import com.certified.covid19response.util.UIState
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message

    val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            try {
                repository.sendPasswordResetEmail(email).await()
                uiState.set(UIState.SUCCESS)
                _message.value = "An email reset link has been to sent to $email"
                _success.value = true
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                _message.value = "An error occurred: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }
}