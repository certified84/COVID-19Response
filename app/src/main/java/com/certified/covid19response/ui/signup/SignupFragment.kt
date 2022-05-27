package com.certified.covid19response.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.certified.covid19response.R
import com.certified.covid19response.data.model.User
import com.certified.covid19response.databinding.FragmentSignupBinding
import com.certified.covid19response.util.Extensions.checkFieldEmpty
import com.certified.covid19response.util.Extensions.showToast
import com.certified.covid19response.util.UIState
import com.certified.covid19response.util.verifyPassword
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignupViewModel by viewModels()

    private lateinit var name: String
    private lateinit var location: String
    private lateinit var nin: String

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

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState

        viewModel.apply {
            message.observe(viewLifecycleOwner) {
                if (it != null) {
                    showToast(it)
                    _message.postValue(null)
                }
            }
            success.observe(viewLifecycleOwner) {
                if (it) {
                    _success.postValue(false)
                    val currentUser = Firebase.auth.currentUser!!
                    val newUser = User(
                        id = currentUser.uid,
                        name = name,
                        email = currentUser.email.toString(),
                        location = location,
                        nin = nin
                    )
                    uploadDetails(currentUser.uid, newUser)
                }
            }
            uploadSuccess.observe(viewLifecycleOwner) {
                if (it) {
                    _uploadSuccess.postValue(false)
                    Firebase.auth.apply {
                        val profileChangeRequest = userProfileChangeRequest {
                            displayName = name.substringAfter("D_")
                        }
                        currentUser!!.updateProfile(profileChangeRequest)
                        currentUser!!.sendEmailVerification()
                        signOut()
                    }
                    findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
                }
            }
        }

        binding.apply {
            btnLogin.setOnClickListener { findNavController().navigate(R.id.action_signupFragment_to_loginFragment) }

            btnSignup.setOnClickListener {
                name = etDisplayName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                location = etLocation.text.toString().trim()
                nin = etNin.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (etDisplayName.checkFieldEmpty())
                    return@setOnClickListener

                if (etEmail.checkFieldEmpty())
                    return@setOnClickListener

                checkEmail(email)

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

                if (!verifyPassword(password, etPassword))
                    return@setOnClickListener

                viewModel.uiState.set(UIState.LOADING)
                viewModel.createUserWithEmailAndPassword(email, password)
            }
        }
    }

    private fun checkEmail(email: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.apply {
                error = "Enter a valid email"
                requestFocus()
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