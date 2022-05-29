package com.certified.covid19response.ui.result

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.covid19response.adapter.DoctorAdapter
import com.certified.covid19response.data.model.Conversation
import com.certified.covid19response.data.model.User
import com.certified.covid19response.databinding.FragmentResultBinding
import com.certified.covid19response.util.Extensions.openBrowser
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint

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

            val adapter = DoctorAdapter()
            adapter.setOnItemClickedListener(object : DoctorAdapter.OnItemClickedListener {
                override fun onItemClick(doctor: User) {
                    findNavController().navigate(
                        ResultFragmentDirections.actionResultFragmentToChatFragment(
                            conversation = Conversation(
                                sender = args.user,
                                receiver = doctor
                            ), message = args.result.feeling
                        )
                    )
                }
            })

            recyclerViewDoctors.adapter = adapter
            recyclerViewDoctors.layoutManager =
                LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}