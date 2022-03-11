package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.DoctorConversation
import com.certified.covid19response.databinding.ItemDoctorChatListBinding

class DoctorChatListRecyclerAdapter :
    ListAdapter<DoctorConversation, DoctorChatListRecyclerAdapter.ViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickedListener

    inner class ViewHolder(val binding: ItemDoctorChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(conversation: DoctorConversation) {
            binding.conversation = conversation
        }

        init {
            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(conversation: DoctorConversation)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<DoctorConversation>() {
            override fun areItemsTheSame(oldItem: DoctorConversation, newItem: DoctorConversation) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DoctorConversation, newItem: DoctorConversation) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDoctorChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}