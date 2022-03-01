package com.certified.covid19response.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.certified.covid19response.R
import com.certified.covid19response.databinding.FragmentOnboardingBinding
import com.certified.covid19response.util.Extensions.openBrowser
import me.ibrahimsn.lib.SmoothBottomBar

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

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
            btnPrivacyPolicy.setOnClickListener {
                requireContext().openBrowser(
                    "https://github.com/certified84", findNavController(),
                    OnboardingFragmentDirections.actionOnboardingFragmentToWebFragment("https://github.com/certified84")
                )
            }
            btnTerms.setOnClickListener {
                requireContext().openBrowser(
                    "https://github.com/certified84/AudioNote", findNavController(),
                    OnboardingFragmentDirections.actionOnboardingFragmentToWebFragment("https://github.com/certified84/AudioNote")
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<SmoothBottomBar>(R.id.bottomBar).visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}