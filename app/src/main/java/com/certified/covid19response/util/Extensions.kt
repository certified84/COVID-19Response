package com.certified.covid19response.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.certified.covid19response.R
import com.certified.covid19response.util.Config.CUSTOM_PACKAGE_NAME
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

    fun Context.openBrowser(url: String, navController: NavController?, directions: NavDirections?) {
        try {
            val packageManager = this.packageManager
            packageManager.getPackageInfo(CUSTOM_PACKAGE_NAME, 0)
            showChromeCustomTabView(url, this)
        } catch (e: PackageManager.NameNotFoundException) {
            navController?.navigate(directions!!)
        }
    }

    private fun showChromeCustomTabView(url: String, context: Context) {
        var customTabsClient: CustomTabsClient?
        var customTabsSession: CustomTabsSession? = null
        val customTabsServiceConnection: CustomTabsServiceConnection =
            object : CustomTabsServiceConnection() {
                override fun onServiceDisconnected(name: ComponentName?) {
                    customTabsClient = null
                }

                override fun onCustomTabsServiceConnected(
                    name: ComponentName,
                    client: CustomTabsClient
                ) {
                    customTabsClient = client
                    customTabsClient!!.warmup(0L)
                    customTabsSession = customTabsClient!!.newSession(null)
                }
            }
        CustomTabsClient.bindCustomTabsService(
            context,
            CUSTOM_PACKAGE_NAME,
            customTabsServiceConnection
        )
        val customTabsIntent = CustomTabsIntent.Builder(customTabsSession)
            .setShowTitle(true).setToolbarColor(
                ResourcesCompat.getColor(
                    context.resources,
                    R.color.colorPrimary,
                    null
                )
            ).build()

        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
}