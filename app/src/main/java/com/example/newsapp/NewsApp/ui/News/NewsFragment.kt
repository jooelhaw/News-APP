package com.example.newsapp.NewsApp.ui.News

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.NewsApp.api.model.ApiManager
import com.example.newsapp.NewsApp.api.model.newsResponse.NewsResponse
import com.example.newsapp.NewsApp.api.model.sourcesResponse.SourceResponse
import com.example.newsapp.NewsApp.api.model.sourcesResponse.Source
import com.example.newsapp.NewsApp.ui.showMessage
import com.example.newsapp.databinding.FragmentNewsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
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
        initView()
        getNewsSources()
    }

    val adapter = NewsAdapter()
    private fun initView() {
        viewBinding.recyclerView.adapter = adapter
    }

    private fun getNewsSources() {
      //  val response = ApiManager.getApis().getSources().execute()
        viewBinding.progressBar.isVisible = true
      //  viewBinding.progressBar.visibility = View.VISIBLE
        ApiManager.getApis().getSources().enqueue(object : Callback<SourceResponse>{
            override fun onFailure(call: Call<SourceResponse>, t: Throwable) {
                viewBinding.progressBar.isVisible = false
                handleError(t) {
                    getNewsSources()
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
                    val response = Gson().fromJson(errorBodyJsonString, SourceResponse::class.java)
                    handleError(response.message){
                        getNewsSources()
                    }
                }

            }
        })
    }
    fun interface OnTryAgainClickListener{
        fun onTryAgainClick()
    }

    private fun handleError(t: Throwable, onClick: OnTryAgainClickListener) {
        showMessage(
            message = t.localizedMessage?:"Something went wrong",
            posActionName = "Try Again",
            posAction = { dialogInterface, i ->
                dialogInterface.dismiss()
                 onClick.onTryAgainClick()
            },
            negActionName = "Cancel"
        ) { dialogInterface, i ->
            dialogInterface.dismiss()
        }
    }
    private fun handleError(message: String?,onClick: OnTryAgainClickListener){
        showMessage(
            message = message?:"Something went wrong",
            posActionName = "Try Again",
            posAction = { dialogInterface, i ->
                dialogInterface.dismiss()
                onClick.onTryAgainClick()
            },
            negActionName = "Cancel"
        ) { dialogInterface, i ->
            dialogInterface.dismiss()
        }
    }

    private fun bindTabs(sources: List<Source?>?) {
        if (sources==null)return
        sources?.forEach{sourcesItem ->
            val tab = viewBinding.tabLayout.newTab()
            tab.text = sourcesItem?.name
            tab.tag = sourcesItem
            viewBinding.tabLayout.addTab(tab)
        }
        viewBinding.tabLayout.addOnTabSelectedListener(
            object : OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val source = tab?.tag as Source
                    getNews(source.id)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val source = tab?.tag as Source
                    getNews(source.id)
                }

            }
        )
        viewBinding.tabLayout.getTabAt(0)?.select()

    }

    private fun getNews(id: String?) {
        viewBinding.progressBar.isVisible = true
        ApiManager.getApis().getNews(Sources = id?:"").enqueue(object : Callback<NewsResponse>{
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                viewBinding.progressBar.isVisible = false
                handleError(t) {
                    getNews(id)
                }
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                viewBinding.progressBar.isVisible = false
                if (response.isSuccessful){
                    adapter.bindNews(response.body()?.articles)
                    return
                }
                val responseJsonError = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(responseJsonError,NewsResponse::class.java)
                handleError(errorResponse.message) {
                    getNews(id)
                }
            }
        })
    }


}