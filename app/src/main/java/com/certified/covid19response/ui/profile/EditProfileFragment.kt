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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()
    private val args: EditProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            which = args.which
            when (args.which) {
                "Edit Name" -> {
                    description = "Kindly provide your first and last name"
                }
                "Edit NIN" -> {
                    description = "Kindly provide your NIN"
                }
                "Add Bio" -> {
                    description = "Kindly add your bio"
                }
            }
            btnBack.setOnClickListener { findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment()) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}