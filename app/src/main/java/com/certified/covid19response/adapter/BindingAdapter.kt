package com.certified.covid19response.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.Article
import com.certified.covid19response.data.model.News
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("listNews")
fun bindNewsRecyclerView(
    recyclerView: RecyclerView,
    data: List<News>?
) {
//    val adapter = recyclerView.adapter as NewsRecyclerAdapter
//    adapter.submitList(data)
}

@BindingAdapter("listArticles")
fun bindArticlesRecyclerView(
    recyclerView: RecyclerView,
    data: List<Article>?
) {
//    val adapter = recyclerView.adapter as ArticlesRecyclerAdapter
//    adapter.submitList(data)
}

@BindingAdapter("severePercentText")
fun MaterialTextView.severePercentText(value: Float) {
    text = "$value% Sever symptoms"
}

@BindingAdapter("lessPercentText")
fun MaterialTextView.lessPercentText(value: Float) {
    text = "$value% Less common symptoms"
}

@BindingAdapter("mostPercentText")
fun MaterialTextView.mostPercentText(value: Float) {
    text = "$value% Most common symptoms"
}