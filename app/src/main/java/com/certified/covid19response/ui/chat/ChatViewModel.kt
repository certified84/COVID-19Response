package com.certified.covid19response.ui.chat

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.model.Conversation
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
    val chatListUiState = ObservableField(UIState.EMPTY)

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _conversations = MutableLiveData<List<Conversation>>()
    val conversations: LiveData<List<Conversation>> get() = _conversations

    fun getChats(id: String) {
        viewModelScope.launch {
            val query = Firebase.firestore.collection("messages")
                .document(id).collection("messages")
                .orderBy("time", Query.Direction.ASCENDING)
            query.addSnapshotListener { value, error ->
                _messages.value = value?.toObjects(Message::class.java)
                Log.d("TAG", "getChat: ${value?.toObjects(Message::class.java)}")
                error?.printStackTrace()
            }
        }
    }
//    ${auth.currentUser!!.uid}_${args.doctor.id}

    fun getUserConversations(userId: String) {
        viewModelScope.launch {
            val query = Firebase.firestore.collection("last_messages")
                .document(userId).collection("messages")
                .orderBy("date", Query.Direction.DESCENDING)
            query.addSnapshotListener { value, error ->
                if (value != null && !value.isEmpty) {
                    chatListUiState.set(UIState.HAS_DATA)
                    _conversations.value = value.toObjects(Conversation::class.java)
                } else
                    chatListUiState.set(UIState.EMPTY)
                if (error != null) {
                    chatListUiState.set(UIState.EMPTY)
                    error.printStackTrace()
                }
            }
        }
    }
}