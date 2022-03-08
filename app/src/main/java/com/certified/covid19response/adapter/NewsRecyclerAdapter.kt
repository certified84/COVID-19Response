package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.News
import com.certified.covid19response.databinding.ItemArticlesBinding
import com.certified.covid19response.databinding.ItemNewsBinding

class NewsRecyclerAdapter : ListAdapter<News, NewsRecyclerAdapter.ViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickedListener

    inner class ViewHolder(val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.news = news
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(news: News)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News) =
                oldItem.originalUrl == newItem.originalUrl

            override fun areContentsTheSame(oldItem: News, newItem: News) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}