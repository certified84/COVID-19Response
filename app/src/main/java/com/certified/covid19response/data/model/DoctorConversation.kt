package com.certified.covid19response.data.model

import com.certified.covid19response.util.currentDate

data class DoctorConversation(
    val id: String,
    val user: User,
    val message: Message,
    val date: Long = currentDate().timeInMillis,
)