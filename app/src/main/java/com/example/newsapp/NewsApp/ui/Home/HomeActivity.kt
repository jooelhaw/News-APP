package com.example.newsapp.NewsApp.ui.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.newsapp.NewsApp.ui.News.NewsActivity
import com.example.newsapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        },2000)
    }
}