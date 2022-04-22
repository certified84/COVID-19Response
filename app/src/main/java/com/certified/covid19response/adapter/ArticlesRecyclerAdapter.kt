package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.News
import com.certified.covid19response.data.model.NewsApiOrgArticle
import com.certified.covid19response.databinding.ItemArticlesBinding

class ArticlesRecyclerAdapter : ListAdapter<NewsApiOrgArticle, ArticlesRecyclerAdapter.ViewHolder>(diffCallback) {

    private lateinit var listener: OnItemClickedListener

    inner class ViewHolder(val binding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsApiOrgArticle) {
            binding.article = article
        }

        init {
            binding.btnReadMore.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(article: NewsApiOrgArticle)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<NewsApiOrgArticle>() {
            override fun areItemsTheSame(oldItem: NewsApiOrgArticle, newItem: NewsApiOrgArticle) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: NewsApiOrgArticle, newItem: NewsApiOrgArticle) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}