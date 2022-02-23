package com.certified.covid19response.util

import android.widget.Toast
import androidx.fragment.app.Fragment
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

    fun Fragment.showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }
}