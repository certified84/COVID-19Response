package com.certified.covid19response.ui.signup

import android.os.Bundle
import android.util.Log
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
import com.certified.covid19response.util.Util
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignupViewModel by viewModels()

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

                if (!Util.verifyPassword(password, etPassword))
                    return@setOnClickListener

                viewModel.uiState.set(UIState.LOADING)
                createUser(email, password, name, location, nin)
            }
        }
    }

    private fun createUser(
        email: String,
        password: String,
        name: String,
        location: String,
        nin: String
    ) {
        viewModel.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val currentUser = Firebase.auth.currentUser!!
                    val newUser = User(
                        id = currentUser.uid,
                        name = name,
                        email = currentUser.email.toString(),
                        location = location,
                        nin = nin
                    )

                    Firebase.firestore.collection("accounts").document("users")
                        .collection(currentUser.uid).document("details").set(newUser)
                        .addOnSuccessListener {
                            val profileChangeRequest = userProfileChangeRequest {
                                displayName = newUser.name
                            }
                            currentUser.updateProfile(profileChangeRequest)
                            currentUser.sendEmailVerification()

                            viewModel.uiState.set(UIState.SUCCESS)
                            showToast("Account created successfully. Check email for verification link")
                            Firebase.auth.signOut()

                            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
                        }
                        .addOnFailureListener {
                            viewModel.uiState.set(UIState.FAILURE)
                            showToast("An error occurred: $it")
                            Log.d("TAG", "onViewCreated: An error occurred $it")
                        }
                } else {
                    viewModel.uiState.set(UIState.FAILURE)
                    showToast("Registration failed ${task.exception}")
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