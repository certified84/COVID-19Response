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

            with(viewModel){message.observe(viewLifecycleOwner) {
                if (it != null) {
                    showToast(it)
                    _message.postValue(null)
                }
            }
            success.observe(viewLifecycleOwner) {
                if (it) {
                    _success.postValue(false)
                    findNavController().navigate(
                        PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToLoginFragment()
                    )
                }
            }}

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
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}