package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.Conversation
import com.certified.covid19response.databinding.ItemReceiverChatListBinding
import com.certified.covid19response.databinding.ItemSenderChatListBinding

class ChatListRecyclerAdapter(private val account_type: String) :
    ListAdapter<Conversation, RecyclerView.ViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickedListener

    inner class SenderViewHolder(val binding: ItemSenderChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(conversation: Conversation) {
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

    inner class ReceiverViewHolder(val binding: ItemReceiverChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(conversation: Conversation) {
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
        fun onItemClick(conversation: Conversation)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Conversation>() {
            override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation) =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (account_type == "user") 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding =
                ItemSenderChatListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            SenderViewHolder(binding)
        } else {
            val binding = ItemReceiverChatListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceiverViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (account_type == "user")
            (holder as SenderViewHolder).bind(currentItem)
        else
            (holder as ReceiverViewHolder).bind(currentItem)
    }
}