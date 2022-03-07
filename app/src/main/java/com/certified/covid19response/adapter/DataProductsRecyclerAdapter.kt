package com.certified.covid19response.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.DataProduct
import com.certified.covid19response.databinding.ItemDataProductBinding
import com.certified.covid19response.databinding.ItemNewsBinding

class DataProductsRecyclerAdapter : ListAdapter<DataProduct, DataProductsRecyclerAdapter.ViewHolder>(diffCallback) {

    inner class ViewHolder(val binding: ItemDataProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataProduct: DataProduct) {
            binding.dataProduct = dataProduct
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<DataProduct>() {
            override fun areItemsTheSame(oldItem: DataProduct, newItem: DataProduct) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DataProduct, newItem: DataProduct) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDataProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}