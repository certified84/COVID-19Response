package com.certified.covid19response.ui.result

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.model.User
import com.certified.covid19response.util.UIState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel() {

    val uiState = ObservableField(UIState.LOADING)

    private val _doctors = MutableLiveData<List<User>?>()
    val doctors: LiveData<List<User>?> get() = _doctors

    init {
        getDoctors()
    }

    private fun getDoctors() {
        viewModelScope.launch {
            val query = Firebase.firestore.collection("users")
            query.addSnapshotListener { value, error ->
                val doctorList =
                    value?.toObjects(User::class.java)?.filter { it.account_type == "doctor" }
                if (doctorList?.isEmpty() == true) uiState.set(UIState.EMPTY)
                else uiState.set(UIState.HAS_DATA)
                _doctors.value = doctorList
                error?.printStackTrace()
            }
        }
    }
}