package com.certified.covid19response.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.certified.covid19response.R
import com.certified.covid19response.data.model.Article
import com.certified.covid19response.data.model.Doctor
import com.certified.covid19response.data.model.News
import com.certified.covid19response.util.Util.roundOffDecimal
import com.google.android.material.imageview.ShapeableImageView
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
    text = "${roundOffDecimal(value)}% Sever symptoms"
}

@BindingAdapter("lessPercentText")
fun MaterialTextView.lessPercentText(value: Float) {
    text = "${roundOffDecimal(value)}% Less common symptoms"
}

@BindingAdapter("mostPercentText")
fun MaterialTextView.mostPercentText(value: Float) {
    text = "${roundOffDecimal(value)}% Most common symptoms"
}

@BindingAdapter("loadImage")
fun ShapeableImageView.loadImage(image: String?) {
    if (image == null)
        this.load(R.drawable.no_profile_image) {
            transformations(CircleCropTransformation())
        }
    else
        this.load(image) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.no_profile_image)
        }
}