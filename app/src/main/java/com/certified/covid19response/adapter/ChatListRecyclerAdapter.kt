package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.UserConversation
import com.certified.covid19response.databinding.ItemChatListBinding

class ChatListRecyclerAdapter :
    ListAdapter<UserConversation, ChatListRecyclerAdapter.ViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickedListener

    inner class ViewHolder(val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(conversation: UserConversation) {
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
        fun onItemClick(conversation: UserConversation)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<UserConversation>() {
            override fun areItemsTheSame(oldItem: UserConversation, newItem: UserConversation) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UserConversation, newItem: UserConversation) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}