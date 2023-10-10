package com.example.newsapp.newsApp.ui

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsapp.R
@BindingAdapter(value = ["app:url", "app:placeholder"], requireAll = false)
fun bindImageWithUrl(imageView: ImageView, url: String?, placeHolder: Drawable?){
    Glide.with(imageView).load(url).placeholder(placeHolder)
        .into(imageView)
}