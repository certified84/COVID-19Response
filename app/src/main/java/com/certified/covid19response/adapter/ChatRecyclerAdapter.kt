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
import com.certified.covid19response.ui.chat.ChatFragment
import timerx.Timer
import timerx.TimerBuilder
import java.util.concurrent.TimeUnit

class ChatRecyclerAdapter(private val id: String) :
    ListAdapter<Message, RecyclerView.ViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickedListener

    inner class ReceiverViewHolder(val binding: ItemChatReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var timer: Timer? = null
        fun bind(message: Message) {
            binding.message = message
        }

        init {
            binding.btnPlayRecording.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val message = getItem(position)
                    if (message.record != null)
                        timer = TimerBuilder()
                            .startTime(message.record.length, TimeUnit.SECONDS)
                            .startFormat("MM:SS")
                            .onTick { time -> binding.tvTimer.text = time }
                            .actionWhen(0, TimeUnit.SECONDS) {
                                timer?.reset()
                                timer?.release()
                            }
                            .build()
                    listener.onItemClick(message, timer)
                }
            }
        }
    }

    inner class SenderViewHolder(val binding: ItemChatSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var timer: Timer? = null
        fun bind(message: Message) {
            binding.message = message
        }

        init {
            binding.btnPlayRecording.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val message = getItem(position)
                    if (message.record != null)
                        timer = TimerBuilder()
                            .startTime(message.record.length, TimeUnit.SECONDS)
                            .startFormat("MM:SS")
                            .onTick { time -> binding.tvTimer.text = time }
                            .actionWhen(0, TimeUnit.SECONDS) {

                            }
                            .build()
                    listener.onItemClick(message, timer)
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
        fun onItemClick(message: Message, timer: Timer?)
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