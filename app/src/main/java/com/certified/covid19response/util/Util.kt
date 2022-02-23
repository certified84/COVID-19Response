package com.certified.covid19response.util

import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

object Util {

    fun verifyPassword(password: String, editText: TextInputEditText): Boolean {

        if (password.length < 8) {
            with(editText) {
                error = "Minimum of 8 characters"
                requestFocus()
            }
            return false
        }

        var numberFlag = false
        var upperCaseFlag = false

        for (i in password.indices) {
            val ch = password[i]
            when {
                ch.isUpperCase() -> {
                    upperCaseFlag = true
                }
                ch.isDigit() -> {
                    numberFlag = true
                }
            }
        }

        if (!upperCaseFlag) {
            with(editText) {
                error = "Uppercase letter required* "
                requestFocus()
            }
            return false
        }

        val pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE)
        if (!pattern.matcher(password).find()) {
            with(editText) {
                error = "Special character required* "
                requestFocus()
            }
            return false
        }

        if (!numberFlag) {
            with(editText) {
                error = "Number required* "
                requestFocus()
            }
            return false
        }

        return true
    }
}