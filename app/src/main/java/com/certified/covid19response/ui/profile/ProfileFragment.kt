package com.certified.covid19response.ui.profile

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import coil.load
import coil.transform.CircleCropTransformation
import com.certified.covid19response.R
import com.certified.covid19response.databinding.DialogEditProfileBinding
import com.certified.covid19response.databinding.FragmentProfileBinding
import com.certified.covid19response.util.Extensions.openBrowser
import com.certified.covid19response.util.Extensions.showToast
import com.certified.covid19response.util.PreferenceKeys
import com.certified.covid19response.util.UIState
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        storage = Firebase.storage
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        binding.apply {

            uiState = viewModel.uiState
            lifecycleOwner = this@ProfileFragment

            viewAccount.setOnClickListener(this@ProfileFragment)
            viewChangePassword.setOnClickListener(this@ProfileFragment)
            viewPrivacy.setOnClickListener(this@ProfileFragment)
            viewHelpSupport.setOnClickListener(this@ProfileFragment)
            btnLogout.setOnClickListener(this@ProfileFragment)
            btnChangeImage.setOnClickListener(this@ProfileFragment)
            btnEditProfile.setOnClickListener(this@ProfileFragment)

            tvUserName.text = auth.currentUser!!.displayName
            Log.d("TAG", "onViewCreated: profileImage: ${auth.currentUser!!.photoUrl}")
            if (auth.currentUser?.photoUrl == null)
                ivProfileImage.load(R.drawable.no_profile_image) {
                    transformations(CircleCropTransformation())
                }
            else
                ivProfileImage.load(auth.currentUser?.photoUrl) {
                    placeholder(R.drawable.no_profile_image)
                    transformations(CircleCropTransformation())
                }

            cbPushNotifications.apply {
                isChecked = preferences.getBoolean(PreferenceKeys.PUSH_NOTIFICATIONS_KEY, false)
                setOnCheckedChangeListener { _, isChecked ->
                    preferences.edit {
                        putBoolean(
                            PreferenceKeys.PUSH_NOTIFICATIONS_KEY,
                            isChecked
                        )
                    }
                }
            }
        }
    }

    private fun launchChangeProfileImageDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val selection = arrayOf(
            "Take picture",
            "Choose from gallery",
        )
        builder.setTitle("Options")
        builder.setSingleChoiceItems(selection, -1) { dialog, which ->
            when (which) {
                0 -> launchCamera()
                1 -> chooseFromGallery()
            }
            dialog.dismiss()
        }
        builder.show()
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            showToast("An error occurred: ${e.localizedMessage}")
        }
    }

    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        try {
            startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            assert(data != null)
            try {
                val bitmap = data?.extras!!["data"] as Bitmap?
                requireContext().openFileOutput("profile_image", Context.MODE_PRIVATE).use {
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                val file = File(requireContext().filesDir, "profile_image")
                uploadImage(Uri.fromFile(file))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            assert(data != null)
            try {
                uploadImage(data?.data)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(uri: Uri?) {
        val path = "profileImages/${auth.currentUser!!.uid}/profileImage.jpg"
        viewModel.apply {
            uiState.set(UIState.LOADING)
            uploadImage(uri, path, storage)
//            val profileImageRef = storage.reference.child(path)
//            profileImageRef.putFile(uri!!).addOnSuccessListener {
//                uiState.set(UIState.SUCCESS)
            binding.ivProfileImage.load(uri) {
                transformations(CircleCropTransformation())
            }
//                showToast("Profile image updated successfully")
//            }. addOnFailureListener {
//                uiState.set(UIState.FAILURE)
//                showToast("An error occurred: ${it.localizedMessage}")
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                viewPrivacy -> {
                    requireContext().openBrowser(
                        "https://github.com/certified84", findNavController(),
                        ProfileFragmentDirections.actionProfileFragmentToWebFragment("https://github.com/certified84")
                    )
                }
                viewHelpSupport -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("Sammie_kt@pm.me"))
                        putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                    }
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    }
                }
                viewChangePassword -> {
                    launchPasswordChangeDialog()
                }
                btnLogout -> {
                    signOut()
                }
                btnChangeImage -> {
                    launchChangeProfileImageDialog()
                }
                btnEditProfile -> {
                    launchBottomSheetDialog()
                }
            }
        }
    }

    private fun signOut() {
        preferences.edit { putBoolean(PreferenceKeys.PUSH_NOTIFICATIONS_KEY, false) }
        auth.signOut()
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
    }

    private fun launchPasswordChangeDialog() {
        val materialDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Password change")
            setMessage("You are about to change your password. A password reset link will be sent to your email and you'll be signed out")
            setPositiveButton("Continue") { dialog, _ ->
                dialog.dismiss()
                viewModel.uiState.set(UIState.LOADING)
                auth.sendPasswordResetEmail(auth.currentUser!!.email!!).addOnSuccessListener {
                    viewModel.uiState.set(UIState.SUCCESS)
                    signOut()
                }.addOnFailureListener {
                    viewModel.uiState.set(UIState.FAILURE)
                    showToast("An error occurred: ${it.localizedMessage}")
                }
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        }
        materialDialog.show()
    }

    private fun launchBottomSheetDialog() {
        val view = DialogEditProfileBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(view.root)
        view.apply {
            btnEditName.setOnClickListener {
                bottomSheetDialog.dismiss()
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(
                        "Edit Name"
                    )
                )
            }
            btnEditNin.setOnClickListener {
                bottomSheetDialog.dismiss()
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(
                        "Edit NIN"
                    )
                )
            }
            btnAddBio.setOnClickListener {
                bottomSheetDialog.dismiss()
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(
                        "Add Bio"
                    )
                )
            }
        }
        bottomSheetDialog.show()
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 101
        private const val PICK_IMAGE_CODE = 102
    }
}