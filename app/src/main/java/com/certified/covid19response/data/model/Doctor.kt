package com.certified.covid19response.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctor(
    val id: String = "",
    val first_name: String = "",
    val last_name: String = "",
    var full_name: String = "",
    val sex: String = "",
    val position: String = "",
    val profile_image: String? = null
) : Parcelable {
    init {
        full_name = "Doctor $first_name $last_name"
    }
}