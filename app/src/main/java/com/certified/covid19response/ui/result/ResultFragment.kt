package com.certified.covid19response.ui.result

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.covid19response.adapter.DoctorAdapter
import com.certified.covid19response.data.model.Doctor
import com.certified.covid19response.databinding.FragmentResultBinding
import com.certified.covid19response.util.Extensions.openBrowser
import com.certified.covid19response.util.UIState
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()
    private val viewModel: ResultViewModel by viewModels()

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

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState

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

            val pieDataSet = PieDataSet(
                listOf(
                    PieEntry(args.result.severePercent / 100),
                    PieEntry(args.result.lessPercent / 100),
                    PieEntry(args.result.mostPercent / 100)
                ), ""
            )
            pieDataSet.apply {
                sliceSpace = 2f
                valueTextSize = 0f
                colors = listOf(Color.RED, Color.BLUE, Color.YELLOW)
            }
            val descrip = Description()
            descrip.text = ""
            pieChart.apply {
                isRotationEnabled = false
                holeRadius = 2f
                description = descrip
                setTransparentCircleAlpha(0)
                data = PieData(pieDataSet)
                invalidate()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val query = Firebase.firestore.collection("doctors")
        query.addSnapshotListener { value, error ->
            val doctors = value?.toObjects(Doctor::class.java)
            lifecycleScope.launch {
                if (doctors?.isEmpty() == true)
                    viewModel.uiState.set(UIState.EMPTY)
                else {
                    viewModel.uiState.set(UIState.HAS_DATA)
                    val adapter = DoctorAdapter()
                    adapter.apply {
                        submitList(doctors)
                        setOnItemClickedListener(object: DoctorAdapter.OnItemClickedListener {
                            override fun onItemClick(doctor: Doctor) {
                                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToUserChatFragment(doctor))
                            }
                        })
                    }
                    binding.recyclerViewDoctors.adapter = adapter
                    binding.recyclerViewDoctors.layoutManager =
                        LinearLayoutManager(requireContext())
                }
            }
            error?.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}