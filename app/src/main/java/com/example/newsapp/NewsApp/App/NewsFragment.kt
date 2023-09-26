package com.example.newsapp.NewsApp.App

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.NewsApp.api.model.ApiManager
import com.example.newsapp.NewsApp.api.model.SourceResponse
import com.example.newsapp.NewsApp.api.model.SourcesItem
import com.example.newsapp.databinding.FragmentNewsBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment: Fragment() {
    lateinit var viewBinding: FragmentNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentNewsBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNewsSources()
    }

    private fun getNewsSources() {
      //  val response = ApiManager.getApis().getSources().execute()
        viewBinding.progressBar.isVisible = true
      //  viewBinding.progressBar.visibility = View.VISIBLE
        ApiManager.getApis().getSources().enqueue(object : Callback<SourceResponse>{
            override fun onFailure(call: Call<SourceResponse>, t: Throwable) {
                viewBinding.progressBar.isVisible = false
                showMessage(
                    message = t.localizedMessage?:"Something went wrong",
                    posActionName = "Try Again",
                    posAction = { dialogInterface, i ->
                        dialogInterface.dismiss()
                        getNewsSources()
                    },
                    negActionName = "Cancel"
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
            }

            override fun onResponse(
                call: Call<SourceResponse>,
                response: Response<SourceResponse>
            ) {
                viewBinding.progressBar.isVisible = false
                if (response.isSuccessful){
                    bindTabs(response.body()?.sources)
                }else{
                    val errorBodyJsonString = response.errorBody()?.string()
                    val response = Gson().fromJson(errorBodyJsonString,SourceResponse::class.java)
                    showMessage(
                        message = response.message?:"Something went wrong",
                        posActionName = "Try Again",
                        posAction = { dialogInterface, i ->
                            dialogInterface.dismiss()
                            getNewsSources()
                        },
                        negActionName = "Cancel"
                    ) { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                }

            }
        })
    }

    private fun bindTabs(sources: List<SourcesItem?>?) {
        if (sources==null)return
        sources?.forEach{sourcesItem ->
            val tab = viewBinding.tabLayout.newTab()
            tab.text = sourcesItem?.name
            viewBinding.tabLayout.addTab(tab)
        }

    }


}