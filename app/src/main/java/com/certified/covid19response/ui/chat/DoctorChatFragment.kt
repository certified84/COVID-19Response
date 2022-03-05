package com.certified.covid19response.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.certified.covid19response.R
import com.certified.covid19response.databinding.FragmentDoctorChatBinding
import com.certified.covid19response.databinding.FragmentUserChatBinding

class DoctorChatFragment : Fragment() {

    private var _binding: FragmentDoctorChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDoctorChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}