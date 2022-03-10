package com.certified.covid19response.data.model

import com.certified.covid19response.util.currentDate

data class UserConversation(
    val id: String = "",
    val doctor: Doctor = Doctor(),
    val message: Message = Message(),
    val date: Long = currentDate().timeInMillis,
)