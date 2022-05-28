package com.certified.covid19response.data.model

import android.os.Parcelable
import com.certified.covid19response.util.currentDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val id: String = "",
    val message: String = "",
    val time: Long = currentDate().timeInMillis,
    val senderId: String = "",
    val receiverId: String = "",
    val read: Boolean = false
): Parcelable