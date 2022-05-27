package com.certified.covid19response.data.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val profile_image: String? = null,
    val location: String = "",
    val nin: String = "",
    val bio: String = "",
    val sex: String = "",
    val position: String = "",
) : Parcelable {
    @IgnoredOnParcel
    val account_type = if (name.startsWith("D_", ignoreCase = true)) "doctor" else "user"
}