package com.certified.covid19response.data.model

import com.certified.covid19response.util.currentDate

data class Message(
    val id: String = "",
    val message: String = "",
    val date: Long = currentDate().timeInMillis,
    val senderId: String = "",
    val receiverId: String = "",
    val read: Boolean = false
)