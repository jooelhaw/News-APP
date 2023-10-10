package com.example.newsapp.newsApp.ui.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewDetailsBinding
import com.example.newsapp.newsApp.api.model.newsResponse.News

class NewDetailsActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityNewDetailsBinding
    private lateinit var news: News
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNewDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        news = ((intent.getParcelableExtra("news") as? News)!!)
        viewBinding.newsData = news
    }
}