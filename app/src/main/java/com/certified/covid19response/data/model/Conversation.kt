package com.certified.covid19response.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Conversation(
    val id: String = "",
    val sender: User? = null,
    val receiver: User? = null,
    val message: Message = Message(),
    @ServerTimestamp
    val date: Date? = null,
): Parcelable