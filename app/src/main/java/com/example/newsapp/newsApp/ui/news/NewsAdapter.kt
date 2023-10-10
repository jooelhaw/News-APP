package com.example.newsapp.newsApp.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newsapp.newsApp.api.model.newsResponse.News
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(var newsList: List<News?>? = null) : Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(val itemBinding: ItemNewsBinding) : ViewHolder(itemBinding.root){
        fun bind(news: News?){
            itemBinding.news = news
            itemBinding.invalidateAll()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val viewBinding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = newsList?.size ?:0

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList!![position]
        holder.bind(news)


    }

    fun bindNews(articles: List<News?>?) {
        newsList = articles
        notifyDataSetChanged()
    }
}