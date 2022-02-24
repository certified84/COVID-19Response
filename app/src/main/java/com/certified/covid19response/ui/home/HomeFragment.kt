package com.certified.covid19response.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.certified.covid19response.R
import com.certified.covid19response.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<SmoothBottomBar>(R.id.bottomBar).visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}