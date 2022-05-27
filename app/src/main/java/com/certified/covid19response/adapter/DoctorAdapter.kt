package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.Doctor
import com.certified.covid19response.data.model.User
import com.certified.covid19response.databinding.ItemDoctorBinding

class DoctorAdapter :
    ListAdapter<User, DoctorAdapter.ViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickedListener

    inner class ViewHolder(val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: User) {
            binding.doctor = doctor
        }

        init {
            binding.btnMessage.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(doctor: User)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}