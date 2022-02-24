package com.certified.covid19response.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val profile_image: String? = null,
    val location: String = "",
    val nin: String = ""
)