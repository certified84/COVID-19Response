package com.certified.covid19response.data.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseRepository {

    fun createUserWithEmailAndPassword(email: String, password: String) =
        Firebase.auth.createUserWithEmailAndPassword(email, password)

    fun signInWithEmailAndPassword(email: String, password: String) =
        Firebase.auth.signInWithEmailAndPassword(email, password)

    fun uploadImage(uri: Uri?): Task<Void>? {
        val profileChangeRequest = userProfileChangeRequest { photoUri = uri }
        return Firebase.auth.currentUser?.updateProfile(profileChangeRequest)
    }

    fun updateNIN(nin: String, userID: String): Task<Void> {
        return Firebase.firestore.collection("accounts").document(userID).update("nin", nin)
    }

    fun updateBio(bio: String, userID: String): Task<Void> {
        return Firebase.firestore.collection("users").document(userID).update("bio", bio)
    }

    fun updateName(name: String, userID: String): Task<Void> {
        return Firebase.firestore.collection("users").document(userID).update("name", name)
    }
}