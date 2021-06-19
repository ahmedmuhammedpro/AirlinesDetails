package com.ahmed.airlinesdetails.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("textValue")
fun setText(view: TextView, str: String?) {
    if (str.isNullOrEmpty()) {
        view.visibility = View.GONE
    } else {
        view.text = str
    }
}