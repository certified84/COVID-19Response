package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.Doctor
import com.certified.covid19response.databinding.ItemDoctorBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class DoctorAdapter(options: FirestoreRecyclerOptions<Doctor>) :
    FirestoreRecyclerAdapter<Doctor, DoctorAdapter.ViewHolder>(options) {

    private lateinit var listener: OnItemClickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Doctor) {
        holder.bind(model)
    }

    inner class ViewHolder(val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: Doctor) {
            binding.doctor = doctor
        }

        init {
            binding.btnMessage.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(doctor: Doctor)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }
}