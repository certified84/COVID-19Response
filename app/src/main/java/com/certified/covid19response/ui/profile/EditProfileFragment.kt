package com.certified.covid19response.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.certified.covid19response.databinding.FragmentEditProfileBinding
import com.certified.covid19response.util.Extensions.showToast
import com.certified.covid19response.util.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()
    private val args: EditProfileFragmentArgs by navArgs()
    private val required = "Required *"
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            uiState = viewModel.editProfileUiState
            which = args.which
            when (args.which) {
                "Edit Name" -> {
                    description = "Kindly provide your first and last name"
                    groupEditName.visibility = View.VISIBLE
                }
                "Edit NIN" -> {
                    description = "Kindly provide your NIN"
                    groupEditNin.visibility = View.VISIBLE
                }
                "Add Bio" -> {
                    description = "Kindly add your bio"
                    groupEditBio.visibility = View.VISIBLE
                }
            }
            btnBack.setOnClickListener(this@EditProfileFragment)
            btnSaveChanges.setOnClickListener(this@EditProfileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                btnBack -> {
                    findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
                }
                btnSaveChanges -> {
                    updateProfile()
                }
            }
        }
    }

    private fun updateProfile() {
        binding.apply {
            when (args.which) {
                "Edit Name" -> {
                    val firstName = etFirstName.text.toString().trim()
                    val lastName = etLastName.text.toString().trim()

                    if (firstName.isBlank()) {
                        etFirstName.apply {
                            error = required
                            requestFocus()
                        }
                        return
                    }

                    if (lastName.isBlank()) {
                        etLastName.apply {
                            error = required
                            requestFocus()
                        }
                        return
                    }

                    val name = "$firstName $lastName"
                    viewModel.editProfileUiState.set(UIState.LOADING)
                    viewModel.updateName(name, auth.currentUser!!.uid).addOnSuccessListener {
                        val profileChangeRequest = userProfileChangeRequest { displayName = name }
                        auth.currentUser!!.updateProfile(profileChangeRequest)
                            .addOnSuccessListener {
                                viewModel.editProfileUiState.set(UIState.SUCCESS)
                                showToast("Name updated successfully")
                                findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
                            }.addOnFailureListener {
                                viewModel.editProfileUiState.set(UIState.FAILURE)
                                showToast("An error occurred: ${it.localizedMessage}")
                            }
                    }.addOnFailureListener {
                        viewModel.editProfileUiState.set(UIState.FAILURE)
                        showToast("An error occurred: ${it.localizedMessage}")
                    }
                }

                "Edit NIN" -> {
                    val nin = etNin.text.toString().trim()

                    if (nin.isBlank()) {
                        etNin.apply {
                            error = required
                            requestFocus()
                        }
                        return
                    }

                    viewModel.editProfileUiState.set(UIState.LOADING)
                    viewModel.updateNIN(nin, auth.currentUser!!.uid).addOnSuccessListener {
                        viewModel.editProfileUiState.set(UIState.SUCCESS)
                        showToast("NIN updated successfully")
                        findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
                    }.addOnFailureListener {
                        viewModel.editProfileUiState.set(UIState.FAILURE)
                        showToast("An error occurred: ${it.localizedMessage}")
                    }
                }

                "Add Bio" -> {
                    val bio = etBio.text.toString().trim()

                    if (bio.isBlank()) {
                        etBio.apply {
                            error = required
                            requestFocus()
                        }
                        return
                    }

                    viewModel.editProfileUiState.set(UIState.LOADING)
                    viewModel.updateBio(bio, auth.currentUser!!.uid).addOnSuccessListener {
                        viewModel.editProfileUiState.set(UIState.SUCCESS)
                        showToast("Bio updated successfully")
                        findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
                    }.addOnFailureListener {
                        viewModel.editProfileUiState.set(UIState.FAILURE)
                        showToast("An error occurred: ${it.localizedMessage}")
                    }
                }
            }
        }
    }
}