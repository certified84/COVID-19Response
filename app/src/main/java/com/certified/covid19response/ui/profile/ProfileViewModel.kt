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

    private val _message = MutableLiveData<String>()
    private val message: LiveData<String> get() = _message

    fun uploadImage(uri: Uri?, path: String, storage: FirebaseStorage) {
        viewModelScope.launch {
            try {
                val profileImageRef = storage.reference.child(path)
                profileImageRef.putFile(uri!!).await()
                profileImageRef.downloadUrl.await()
                repository.uploadImage(profileImageRef.downloadUrl.await())?.await()
                uiState.set(UIState.SUCCESS)
                _message.value = "Image uploaded successfully"
                Log.d("TAG", "uploadImage: Success: ${profileImageRef.downloadUrl.await()}")
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                _message.value = "An error occurred: ${e.localizedMessage}"
                Log.d("TAG", "uploadImage: Error: ${e.localizedMessage}")
            }
        }
    }

    fun updateNIN(nin: String, userID: String) = repository.updateNIN(nin, userID)

    fun updateBio(bio: String, userID: String) = repository.updateBio(bio, userID)

    fun updateName(name: String, userID: String) = repository.updateName(name, userID)
}