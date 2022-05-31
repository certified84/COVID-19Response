package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.Message
import com.certified.covid19response.databinding.ItemChatDateBinding
import com.certified.covid19response.databinding.ItemChatReceiverBinding
import com.certified.covid19response.databinding.ItemChatSenderBinding

class ChatRecyclerAdapter(private val id: String) :
    ListAdapter<Message, RecyclerView.ViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickedListener

    inner class ReceiverViewHolder(val binding: ItemChatReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
        }

        init {
            binding.btnPlayRecording.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val message = getItem(position)
                    listener.onItemClick(message)
                }
            }
        }
    }

    inner class SenderViewHolder(val binding: ItemChatSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
        }

        init {
            binding.btnPlayRecording.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    inner class ChatDateViewHolder(val binding: ItemChatDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(message: Message)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Message, newItem: Message) =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return if (id == currentItem.senderId) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding =
                ItemChatSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SenderViewHolder(binding)
        } else {
            val binding =
                ItemChatReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceiverViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (id == currentItem.senderId)
            (holder as SenderViewHolder).bind(currentItem)
        else
            (holder as ReceiverViewHolder).bind(currentItem)
    }
}