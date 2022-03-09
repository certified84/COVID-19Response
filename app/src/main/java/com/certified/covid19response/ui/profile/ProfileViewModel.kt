package com.certified.covid19response.ui.profile

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.UIState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)
    val editProfileUiState = ObservableField(UIState.EMPTY)

    val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message

    val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    fun uploadImage(uri: Uri?, path: String, storage: FirebaseStorage, userID: String) {
        viewModelScope.launch {
            try {
                val profileImageRef = storage.reference.child(path)
                profileImageRef.putFile(uri!!).await()
                val downloadUrl = profileImageRef.downloadUrl.await()
                repository.uploadImage(downloadUrl)?.await()
//                repository.updateProfile(userID).await()
                uiState.set(UIState.SUCCESS)
                _message.value = "Image uploaded successfully"
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                _message.value = "An error occurred: ${e.localizedMessage}"
                Log.d("TAG", "uploadImage: Error: ${e.localizedMessage}")
            }
        }
    }

    fun updateNIN(nin: String, userID: String) {
        viewModelScope.launch {
            try {
                repository.updateNIN(nin, userID).await()
                editProfileUiState.set(UIState.SUCCESS)
                _message.value = "NIN updated successfully"
                _success.value = true
            } catch (e: Exception) {
                editProfileUiState.set(UIState.FAILURE)
                _message.value = "An error occurred: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }

    fun updateBio(bio: String, userID: String) {
        viewModelScope.launch {
            try {
                repository.updateBio(bio, userID).await()
                editProfileUiState.set(UIState.SUCCESS)
                _message.value = "Bio updated successfully"
                _success.value = true
            } catch (e: Exception) {
                editProfileUiState.set(UIState.FAILURE)
                _message.value = "An error occurred: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }

    fun updateName(name: String, user: FirebaseUser) {
        viewModelScope.launch {
            try {
                repository.updateName(name, user.uid).await()
                val profileChangeRequest = userProfileChangeRequest { displayName = name }
                user.updateProfile(profileChangeRequest).await()
                editProfileUiState.set(UIState.SUCCESS)
                _message.value = "Name updated successfully"
                _success.value = true
            } catch (e: Exception) {
                editProfileUiState.set(UIState.FAILURE)
                _message.value = "An error occurred: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }
}