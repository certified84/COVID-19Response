package com.certified.covid19response.ui.result

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
import com.certified.covid19response.util.UIState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()
    private val viewModel: ResultViewModel by viewModels()
//    private lateinit var adapter: DoctorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
//        val doctors = ArrayList<Doctor>()
//        val query =
//            Firebase.firestore.collection("doctors")
//        query.addSnapshotListener { value, error ->
//            Log.d("TAG", "onCreateView: Doctors: ${value?.toObjects(Doctor::class.java)}")
////            value?.toObjects(Doctor::class.java)?.let { doctors.addAll(it) }
////            val adapter = DoctorAdapter(value?.toObjects(Doctor::class.java))
//            if (value?.toObjects(Doctor::class.java)?.isEmpty() == true)
//                viewModel.uiState.set(UIState.EMPTY)
////            if (value != null)
////                for (doctor in value.toObjects(Doctor::class.java))
////                    doctors.add(doctor)
//            error?.printStackTrace()
//        }
//        val options =
//            FirestoreRecyclerOptions.Builder<Doctor>().setQuery(query, Doctor::class.java).build()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            result = args.result
            uiState = viewModel.uiState
            btnBack.setOnClickListener { findNavController().navigate(ResultFragmentDirections.actionResultFragmentToStatusFragment()) }
            val query =
                Firebase.firestore.collection("doctors")
            query.addSnapshotListener { value, error ->
                val doctors = value?.toObjects(Doctor::class.java)
                Log.d("TAG", "onCreateView: Doctors: $doctors")
                lifecycleScope.launch {
                    delay(5000L)
                    if (doctors?.isEmpty() == true)
                        viewModel.uiState.set(UIState.EMPTY)
                    else {
                        viewModel.uiState.set(UIState.HAS_DATA)
                        val adapter = DoctorAdapter(doctors!!)
                        recyclerViewDoctors.adapter = adapter
                        recyclerViewDoctors.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
                error?.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()
//        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
//        adapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}