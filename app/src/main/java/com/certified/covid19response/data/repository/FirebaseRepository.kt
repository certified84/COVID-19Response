package com.certified.covid19response.data.repository

import android.net.Uri
import com.certified.covid19response.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseRepository @Inject constructor() {

    fun createUserWithEmailAndPassword(email: String, password: String) =
        Firebase.auth.createUserWithEmailAndPassword(email, password)

    fun uploadDetails(userID: String, newUser: User): Task<Void> {
        return Firebase.firestore.collection("users").document(userID).set(newUser)
    }

    fun signInWithEmailAndPassword(email: String, password: String) =
        Firebase.auth.signInWithEmailAndPassword(email, password)

    fun sendPasswordResetEmail(email: String) =
        Firebase.auth.sendPasswordResetEmail(email)

    fun uploadImage(uri: Uri?): Task<Void>? {
        val profileChangeRequest = userProfileChangeRequest { photoUri = uri }
        return Firebase.auth.currentUser?.updateProfile(profileChangeRequest)
    }

    fun updateProfile(imageUrl: String): Task<Void> {
        return Firebase.firestore.collection("users").document(Firebase.auth.currentUser!!.uid)
            .update("profile_image", imageUrl)
    }

    fun updateNIN(nin: String, userID: String): Task<Void> {
        return Firebase.firestore.collection("users").document(userID).update("nin", nin)
    }

    fun updateBio(bio: String, userID: String): Task<Void> {
        return Firebase.firestore.collection("users").document(userID).update("bio", bio)
    }

    fun updateName(name: String, userID: String): Task<Void> {
        return Firebase.firestore.collection("users").document(userID).update("name", name)
    }
}