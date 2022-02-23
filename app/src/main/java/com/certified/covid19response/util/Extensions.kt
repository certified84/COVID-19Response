package com.certified.covid19response.util

import com.google.android.material.textfield.TextInputEditText

object Extensions {
    fun TextInputEditText.checkFieldEmpty(): Boolean {
        return if (this.text.toString().isBlank()) {
            with(this) {
                error = "Required *"
                requestFocus()
            }
            true
        } else
            false
    }
}