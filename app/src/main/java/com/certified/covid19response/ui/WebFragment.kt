package com.certified.covid19response.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.certified.covid19response.databinding.FragmentWebBinding

class WebFragment : Fragment() {

    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!
    private val args: WebFragmentArgs by navArgs()
    private val CUSTOM_PACKAGE_NAME = "com.android.chrome"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.apply {
            wbWebView.apply {
                webViewClient = WebViewClient()
                loadUrl(args.url)
                settings.javaScriptEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    settings.safeBrowsingEnabled = true
                canGoBack()
            }
        }
//        TODO("Setup onBackPressed")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}