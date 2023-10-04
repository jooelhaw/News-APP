package com.example.newsapp.NewsApp.ui

import com.example.newsapp.NewsApp.ui.News.NewsFragment

data class ViewError(
    val message: String? = null,
    val throwable: Throwable? = null,
    val onTryAgainClickListener: OnTryAgainClickListener? = null
)
fun interface OnTryAgainClickListener{
    fun onTryAgainClick()
}
