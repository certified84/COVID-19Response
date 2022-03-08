package com.certified.covid19response.ui.passwordRecovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.certified.covid19response.databinding.FragmentPasswordRecoveryBinding
import com.certified.covid19response.util.Extensions.showToast
import com.certified.covid19response.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordRecoveryFragment : Fragment() {

    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PasswordRecoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = this@PasswordRecoveryFragment
            binding.uiState = viewModel.uiState
            btnBack.setOnClickListener {
                findNavController().navigate(
                    PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToLoginFragment()
                )
            }
            btnResetPassword.setOnClickListener {
                val email = etEmail.text.toString().trim()

                if (email.isBlank()) {
                    etEmail.error = "Required *"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                viewModel.uiState.set(UIState.LOADING)
                viewModel.sendPasswordResetEmail(email)
                Firebase.auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    viewModel.uiState.set(UIState.SUCCESS)
                    showToast("An email reset link has been to sent to $email")
                    findNavController().navigate(
                        PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToLoginFragment()
                    )
                }.addOnFailureListener {
                    viewModel.uiState.set(UIState.FAILURE)
                    showToast("An error occurred: ${it.localizedMessage}")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}