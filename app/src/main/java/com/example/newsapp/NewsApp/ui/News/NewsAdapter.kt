package com.example.newsapp.NewsApp.ui.News

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp.NewsApp.api.model.newsResponse.News
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(var newsList: List<News?>? = null) : Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(val itemBinding: ItemNewsBinding) : ViewHolder(itemBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val viewBinding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = newsList?.size ?:0

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList!![position]
        holder.itemBinding.title.text = news?.title
        holder.itemBinding.description.text = news?.description
        Glide.with(holder.itemView).load(news?.urlToImage)
            .placeholder(R.drawable.pattern)
            .into(holder.itemBinding.newsImg)

    }

    fun bindNews(articles: List<News?>?) {
        newsList = articles
        notifyDataSetChanged()
    }
}