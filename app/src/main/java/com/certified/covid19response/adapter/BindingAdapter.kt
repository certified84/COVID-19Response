package com.certified.covid19response.adapter

import android.os.Build
import android.text.Html
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.certified.covid19response.R
import com.certified.covid19response.data.model.Conversation
import com.certified.covid19response.data.model.Message
import com.certified.covid19response.data.model.NewsApiOrgArticle
import com.certified.covid19response.data.model.User
import com.certified.covid19response.util.roundOffDecimal
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.imageview.ShapeableImageView
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
    data: List<NewsApiOrgArticle>?
) {
    val adapter = recyclerView.adapter as NewsRecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("listArticles")
fun bindArticlesRecyclerView(
    recyclerView: RecyclerView,
    data: List<NewsApiOrgArticle>?
) {
    val adapter = recyclerView.adapter as ArticlesRecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("listMessages")
fun bindChatRecyclerView(
    recyclerView: RecyclerView,
    data: List<Message>?
) {
    val adapter = recyclerView.adapter as ChatRecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("listChats")
fun bindChatListRecyclerView(
    recyclerView: RecyclerView,
    data: List<Conversation>?
) {
    val adapter = recyclerView.adapter as ChatListRecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDoctors")
fun bindDoctorsRecyclerView(
    recyclerView: RecyclerView,
    data: List<User>?
) {
    val adapter = recyclerView.adapter as DoctorAdapter
    adapter.submitList(data)
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
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")).toString()
    else
        time
}

@BindingAdapter("load_image")
fun ShapeableImageView.loadImage(image: String?) {
    if (image != null && image.isNotBlank()) this.load(image) {
        CircleCropTransformation()
        placeholder(R.drawable.no_profile_image)
    }
    else this.load(R.drawable.no_profile_image)
}

@BindingAdapter("load_newsImage")
fun ShapeableImageView.loadNewsImage(image: String?) {
    if (image != null && image.isNotBlank()) this.load(image) {
        RoundedCornersTransformation(0f)
        placeholder(R.drawable.no_profile_image)
    }
    else this.load(R.drawable.no_profile_image)
}

@BindingAdapter("load_chatImage")
fun AppCompatImageView.loadChatImage(image: String?) {
    if (image != null && image.isNotBlank()) this.load(image) {
        RoundedCornersTransformation(0f)
    }
}

@BindingAdapter("set_animation")
fun ShimmerFrameLayout.setShimmerAnimation(visible: Boolean) {
    if (visible) startShimmerAnimation() else stopShimmerAnimation()
}

@BindingAdapter("doctor_name")
fun MaterialTextView.doctorName(value: String) {
    text = "Doctor ${value.substringAfter("D_")}"
}

@BindingAdapter("timeText")
fun MaterialTextView.timeText(value: Long) {
    text = if (value >= 3600)
        String.format("%02d:%02d:%02d", value / 3600, (value % 3600) / 60, value % 60)
    else
        String.format("%02d:%02d", (value % 3600) / 60, value % 60)
}