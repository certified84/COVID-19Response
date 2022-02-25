package com.certified.covid19response.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.certified.covid19response.databinding.FragmentProfileBinding
import com.certified.covid19response.util.Extensions.openBrowser
import com.certified.covid19response.util.PreferenceKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

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
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        binding.apply {

            viewAccount.setOnClickListener(this@ProfileFragment)
            viewChangePassword.setOnClickListener(this@ProfileFragment)
            viewPrivacy.setOnClickListener(this@ProfileFragment)
            viewHelpSupport.setOnClickListener(this@ProfileFragment)
            btnLogout.setOnClickListener(this@ProfileFragment)

            tvUserName.text = auth.currentUser?.displayName

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
                btnLogout -> {
                    auth.signOut()
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
                }
            }
        }
    }
}