package com.certified.covid19response.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.certified.covid19response.R
import com.certified.covid19response.databinding.FragmentLoginBinding
import com.certified.covid19response.util.Extensions.checkFieldEmpty
import com.certified.covid19response.util.Extensions.showToast
import com.certified.covid19response.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState
        binding.apply {

            btnForgotButton.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToPasswordRecoveryFragment()
                )
            }

            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (etEmail.checkFieldEmpty())
                    return@setOnClickListener

                if (etPassword.checkFieldEmpty())
                    return@setOnClickListener

                viewModel.uiState.set(UIState.LOADING)
                viewModel.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val user = Firebase.auth.currentUser!!

                            if (user.isEmailVerified)
                                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                            else {
                                viewModel.uiState.set(UIState.FAILURE)
                                Firebase.auth.signOut()
                                showToast("Check your email for verification link")
                            }
                        } else {
                            viewModel.uiState.set(UIState.FAILURE)
                            showToast("Authentication failed. ${task.exception}")
                        }
                    }
            }

            btnSignup.setOnClickListener { findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment()) }
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