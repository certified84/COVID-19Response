package com.certified.covid19response.ui.chat

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.covid19response.data.model.Conversation
import com.certified.covid19response.data.model.Message
import com.certified.covid19response.data.model.Record
import com.certified.covid19response.data.model.User
import com.certified.covid19response.data.repository.FirebaseRepository
import com.certified.covid19response.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: FirebaseRepository) : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)
    val chatListUiState = ObservableField(UIState.EMPTY)

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _conversations = MutableLiveData<List<Conversation>>()
    val conversations: LiveData<List<Conversation>> get() = _conversations

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

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

    fun getUserConversations(userId: String) {
        viewModelScope.launch {
            val query = Firebase.firestore.collection("messages")
                .document("last_messages").collection(userId)
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

    fun sendMessage(id: String, text: String, sender: User?, receiver: User?, message: Message) {

        val db = Firebase.firestore

        val messageToSend = message.copy(
            id = id,
            message = text,
            senderId = Firebase.auth.currentUser!!.uid,
            receiverId = receiver!!.id
        )

        val messagesRef = db.collection("messages").document(id).collection("messages").document()
        val senderLastMessageRef =
            db.collection("messages").document("last_messages").collection(sender!!.id).document(id)
        val receiverLastMessageRef =
            db.collection("messages").document("last_messages").collection(receiver.id).document(id)
        db.runBatch {
            it.set(messagesRef, messageToSend)
            it.set(
                senderLastMessageRef,
                Conversation(
                    id = senderLastMessageRef.id,
                    sender = sender,
                    receiver = receiver,
                    message = messageToSend
                )
            )
            it.set(
                receiverLastMessageRef,
                Conversation(
                    id = receiverLastMessageRef.id,
                    sender = sender,
                    receiver = receiver,
                    message = messageToSend
                )
            )
        }.addOnFailureListener {
            _toastMessage.value = "An error occurred: ${it.localizedMessage}"
        }
    }

    fun uploadImage(
        uri: Uri?,
        path: String,
        storage: FirebaseStorage,
        id: String,
        text: String,
        sender: User?,
        receiver: User?,
        message: Message
    ) {
        viewModelScope.launch {
            try {
                val imageRef = storage.reference.child(path)
                imageRef.putFile(uri!!).await()
                val downloadUrl = imageRef.downloadUrl.await()
                sendMessage(id, "", sender, receiver, Message(image = downloadUrl.toString()))
                uiState.set(UIState.SUCCESS)
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                Log.d("TAG", "uploadImage: Error: ${e.localizedMessage}")
            }
        }
    }
}