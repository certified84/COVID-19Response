package com.certified.covid19response.ui

import android.content.ComponentName
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.certified.covid19response.R
import com.certified.covid19response.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val CUSTOM_PACKAGE_NAME = "com.android.chrome"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogin.setOnClickListener { findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment) }
            btnSignup.setOnClickListener { findNavController().navigate(R.id.action_onboardingFragment_to_signupFragment) }
            btnPrivacyPolicy.setOnClickListener { openBrowser("https://github.com/certified84") }
            btnTerms.setOnClickListener { openBrowser("https://github.com/certified84/AudioNote") }
        }
    }

    private fun openBrowser(url: String) {
        try {
            val packageManager = requireContext().packageManager
            packageManager.getPackageInfo(CUSTOM_PACKAGE_NAME, 0)
            showChromeCustomTabView(url)
        } catch (e: PackageManager.NameNotFoundException) {
            findNavController().navigate(
                OnboardingFragmentDirections.actionOnboardingFragmentToWebFragment(url)
            )
        }
    }

    private fun showChromeCustomTabView(url: String) {
        var customTabsClient: CustomTabsClient? = null
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
            requireContext(),
            CUSTOM_PACKAGE_NAME,
            customTabsServiceConnection
        )
        val customTabsIntent = CustomTabsIntent.Builder(customTabsSession)
            .setShowTitle(true).setToolbarColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.colorPrimary,
                    null
                )
            ).build()

        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}