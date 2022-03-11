package com.certified.covid19response.data.model

import com.certified.covid19response.util.currentDate

data class DoctorConversation(
    val id: String = "",
    val user: User = User(),
    val message: Message = Message(),
    val date: Long = currentDate().timeInMillis,
)