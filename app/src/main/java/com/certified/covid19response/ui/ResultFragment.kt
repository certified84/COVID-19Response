package com.certified.covid19response.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.certified.covid19response.databinding.FragmentResultBinding
import com.certified.covid19response.util.Extensions.openBrowser

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            result = args.result
            btnBack.setOnClickListener { findNavController().navigate(ResultFragmentDirections.actionResultFragmentToStatusFragment()) }
            btnCovidIsolationLink.setOnClickListener {
                val url =
                    "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public"
                requireContext().openBrowser(
                    url,
                    findNavController(),
                    ResultFragmentDirections.actionResultFragmentToWebFragment(url)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}