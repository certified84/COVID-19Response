package com.certified.covid19response.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.Doctor
import com.certified.covid19response.databinding.ItemDoctorBinding

class DoctorAdapter(private val doctors: MutableList<Doctor>) :
    ListAdapter<Doctor, DoctorAdapter.ViewHolder>(diffCallback) {

    init {
        Log.d("TAG", "DoctorAdapter: Initialized")
    }

    private lateinit var listener: OnItemClickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = doctors[position]
        Log.d("TAG", "onBindViewHolder: Doctor: $currentItem")
        holder.bind(currentItem)
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

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Doctor>() {
            override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor) = oldItem == newItem
        }
    }
}