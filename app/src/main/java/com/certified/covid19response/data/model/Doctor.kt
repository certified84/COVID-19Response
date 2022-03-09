package com.certified.covid19response.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctor(
    val id: String = "",
    val name: String = "",
    val sex: String = "",
    val position: String = "",
    val profile_image: String? = null
) : Parcelable