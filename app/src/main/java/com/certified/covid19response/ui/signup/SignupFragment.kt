package com.certified.covid19response.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.certified.covid19response.R
import com.certified.covid19response.databinding.FragmentSignupBinding
import com.certified.covid19response.util.Extensions.checkFieldEmpty
import com.google.android.material.textfield.TextInputEditText

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnLogin.setOnClickListener { findNavController().navigate(R.id.action_signupFragment_to_loginFragment) }

            btnSignup.setOnClickListener {
                val name = etDisplayName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val location = etLocation.text.toString().trim()
                val nin = etNin.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (etDisplayName.checkFieldEmpty())
                    return@setOnClickListener

                if (etEmail.checkFieldEmpty())
                    return@setOnClickListener

                if (location.isBlank()) {
                    with(etLocation) {
                        error = "Required *"
                        requestFocus()
                    }
                    return@setOnClickListener
                }

                if (etNin.checkFieldEmpty())
                    return@setOnClickListener

                if (etPassword.checkFieldEmpty())
                    return@setOnClickListener

                verifyPassword(password)
            }
        }
    }

    private fun checkFieldEmpty(editText: TextInputEditText) {
        if (editText.text.toString().trim().isBlank()) {
            with(editText) {
                error = "Required *"
                requestFocus()
            }
            return
        }
    }

    private fun verifyPassword(password: String) {

        binding.apply {
            if (password.length < 8) {
                with(etPassword) {
                    error = "Minimum of 8 characters"
                    requestFocus()
                }
                return
            }

            var numberFlag = false
            var upperCaseFlag = false

            for (i in password.indices) {
                val ch = password[i]
                when {
                    ch.isDigit() -> {
                        numberFlag = true
                    }
                    ch.isUpperCase() -> {
                        upperCaseFlag = true
                    }
                }
            }

            if (!numberFlag) {
                with(etPassword) {
                    error = "A number is required"
                    requestFocus()
                }
                return
            }

            if (!upperCaseFlag) {
                with(etPassword) {
                    error = "A capital letter is required"
                    requestFocus()
                }
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val locations = resources.getStringArray(R.array.locations)
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            locations
        )
        binding.etLocation.setAdapter(arrayAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}