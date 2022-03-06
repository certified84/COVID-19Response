package com.certified.covid19response.adapter

import android.os.Build
import android.text.Html
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.covid19response.data.model.Article
import com.certified.covid19response.data.model.DataProduct
import com.certified.covid19response.data.model.News
import com.certified.covid19response.util.Util.roundOffDecimal
import com.google.android.material.textview.MaterialTextView
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

@BindingAdapter("listDataProducts")
fun bindDataProductRecyclerView(
    recyclerView: RecyclerView,
    data: List<DataProduct>?
) {
    val adapter = recyclerView.adapter as DataProductsRecyclerAdapter
    adapter.submitList(data)
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

@BindingAdapter("parse_html")
fun MaterialTextView.parseHtml(htmlText: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
    else
        Html.fromHtml(htmlText)
}

@BindingAdapter("server_time")
fun MaterialTextView.parseServerTime(time: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        ZonedDateTime.parse(time)
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd/MM/YYYY hh:mm a")).toString()
    else
        time
}