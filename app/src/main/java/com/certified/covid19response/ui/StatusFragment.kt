package com.certified.covid19response.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.certified.covid19response.databinding.DialogCovidStatusBinding
import com.certified.covid19response.databinding.FragmentStatusBinding
import com.certified.covid19response.util.Extensions.openBrowser
import com.google.android.material.bottomsheet.BottomSheetDialog

class StatusFragment : Fragment() {

    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSymptomsLink.setOnClickListener {
                requireContext().openBrowser(
                    "https://www.who.int/health-topics/coronavirus#tab=tab_3", findNavController(),
                    StatusFragmentDirections.actionStatusFragmentToWebFragment("https://www.who.int/health-topics/coronavirus#tab=tab_3")
                )
            }

            btnCheckStatus.setOnClickListener {
                launchBottomSheetDialog()
            }
        }
    }

    private fun launchBottomSheetDialog() {
        val view = DialogCovidStatusBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(view.root)
        view.apply {
            btnCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnContinue.setOnClickListener {
                bottomSheetDialog.dismiss()
                findNavController().navigate(StatusFragmentDirections.actionStatusFragmentToResultFragment())
            }
        }
        bottomSheetDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}