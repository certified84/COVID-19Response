package com.certified.covid19response.ui.profile

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)
    val editProfileUiState = ObservableField(UIState.EMPTY)

    fun uploadImage(uri: Uri?) = repository.uploadImage(uri)

    fun updateNIN(nin: String, userID: String) = repository.updateNIN(nin, userID)

    fun updateBio(bio: String, userID: String) = repository.updateBio(bio, userID)

    fun updateName(name: String, userID: String) = repository.updateName(name, userID)
}