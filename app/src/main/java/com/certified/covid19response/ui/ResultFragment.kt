package com.certified.covid19response.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.certified.covid19response.adapter.DoctorAdapter
import com.certified.covid19response.data.model.Doctor
import com.certified.covid19response.databinding.FragmentResultBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()
    private lateinit var adapter: DoctorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val query =
            Firebase.firestore.collection("doctors").orderBy("id")
        val options =
            FirestoreRecyclerOptions.Builder<Doctor>().setQuery(query, Doctor::class.java).build()
        adapter = DoctorAdapter(options)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            result = args.result
            btnBack.setOnClickListener { findNavController().navigate(ResultFragmentDirections.actionResultFragmentToStatusFragment()) }
            recyclerViewDoctors.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}