package com.certified.covid19response.adapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}