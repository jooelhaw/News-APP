package com.example.newsapp.newsApp.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsapp.R
@BindingAdapter(value = ["app:url", "app:placeholder"], requireAll = false)
fun bindImageWithUrl(imageView: ImageView, url: String?, placeHolder: Drawable?){
    Glide.with(imageView).load(url).placeholder(placeHolder)
        .into(imageView)
}

@BindingAdapter("imageUrl")
fun bindImageWithUrl(imageView: ImageView, url: String?){
    Glide.with(imageView).load(url)
        .into(imageView)
}

@BindingAdapter("launchUrl")
fun clicktoLaunchUrl(view: View, url: String?){
    view.setOnClickListener {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }
}