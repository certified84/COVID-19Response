package com.certified.covid19response.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.UIState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult>? {
        return try {
            repository.createUserWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}