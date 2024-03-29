package com.certified.covid19response.util

import android.app.Activity
import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.vmadalin.easypermissions.EasyPermissions
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

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

fun roundOffDecimal(number: Float): Float {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toFloat()
}

fun currentDate(): Calendar = Calendar.getInstance()

fun formatTime(date: Date?): String =
    if (date != null) SimpleDateFormat("h:mm a", Locale.getDefault()).format(date) else ""

fun formatChatDate(date: Date): String {
    val day = TimeUnit.MILLISECONDS.toDays(Date().time - date.time)
    return when {
        day <= 1 -> "Today"
        day <= 2 -> "Yesterday"
        else -> SimpleDateFormat("MMM, DD", Locale.getDefault()).format(date)
    }
//    SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
}

fun filePath(activity: Activity) = activity.getExternalFilesDir("/")?.absolutePath

fun hasPermission(context: Context, permission: String) =
    EasyPermissions.hasPermissions(context, permission)

fun requestPermission(activity: Activity, message: String, requestCode: Int, permission: String) {
    EasyPermissions.requestPermissions(activity, message, requestCode, permission)
}