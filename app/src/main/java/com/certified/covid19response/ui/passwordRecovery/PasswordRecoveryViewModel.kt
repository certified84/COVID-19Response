package com.certified.covid19response.ui.passwordRecovery

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.UIState
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    fun sendPasswordResetEmail(email: String): Task<Void>? {
        return try {
            repository.sendPasswordResetEmail(email)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}