package com.certified.covid19response.ui.chat

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.model.Message
import com.certified.covid19response.util.UIState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun getChat(id: String) {
        viewModelScope.launch {
            val query = Firebase.firestore.collection("messages")
                .document(id).collection("messages")
                .orderBy("date", Query.Direction.ASCENDING)
            query.addSnapshotListener { value, error ->
                _messages.value = value?.toObjects(Message::class.java)
                error?.printStackTrace()
            }
        }
    }
//    ${auth.currentUser!!.uid}_${args.doctor.id}
}