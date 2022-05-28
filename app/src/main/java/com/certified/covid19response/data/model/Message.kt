package com.certified.covid19response.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Message(
    val id: String = "",
    val message: String = "",
    @ServerTimestamp
    val time: Date? = null,
    val senderId: String = "",
    val receiverId: String = "",
    val read: Boolean = false
) : Parcelable