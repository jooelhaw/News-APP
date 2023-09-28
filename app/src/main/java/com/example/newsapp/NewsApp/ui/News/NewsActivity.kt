package com.example.newsapp.NewsApp.ui.News

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityNewsBinding
    lateinit var category: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        category = intent.getStringExtra("CATEGORY").toString()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NewsFragment(category)).commit()

    }
}