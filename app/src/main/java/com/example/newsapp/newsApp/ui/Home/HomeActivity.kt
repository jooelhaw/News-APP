package com.example.newsapp.newsApp.ui.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.newsApp.ui.news.NewsActivity
import com.example.newsapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        val intent = Intent(this,NewsActivity::class.java)
        setContentView(viewBinding.root)
        viewBinding.sports.setOnClickListener {
            intent.putExtra("CATEGORY","sports")
            startActivity(intent)
        }
        viewBinding.business.setOnClickListener {
            intent.putExtra("CATEGORY","business")
            startActivity(intent)
        }
        viewBinding.health.setOnClickListener {
            intent.putExtra("CATEGORY","health")
            startActivity(intent)
        }
        viewBinding.environment.setOnClickListener {
            intent.putExtra("CATEGORY","entertainment")
            startActivity(intent)
        }
        viewBinding.technology.setOnClickListener {
            intent.putExtra("CATEGORY","technology")
            startActivity(intent)
        }
        viewBinding.science.setOnClickListener {
            intent.putExtra("CATEGORY","science")
            startActivity(intent)
        }
    }
}