package com.certified.covid19response.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.model.AccountType
import com.certified.covid19response.data.model.User
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message

    val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean> get() = _uploadSuccess

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                repository.createUserWithEmailAndPassword(email, password).await()
                _success.value = true
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                _message.value = "Registration failed: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }

    fun uploadDetails(userID: String, newUser: User) {
        viewModelScope.launch {
            try {
                repository.uploadDetails(userID, newUser).await()
                repository.setAccountType(userID, AccountType()).await()
                uiState.set(UIState.SUCCESS)
                _uploadSuccess.value = true
                _message.value =
                    "Account created successfully. We sent a verification link to ${newUser.email}"
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                _uploadSuccess.value = false
            }
        }
    }
}