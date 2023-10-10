package com.example.newsapp.newsApp.ui.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.newsApp.api.model.sourcesResponse.Source
import com.example.newsapp.newsApp.ui.Splash
import com.example.newsapp.newsApp.ui.ViewError
import com.example.newsapp.newsApp.ui.showMessage
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.newsApp.api.model.newsResponse.News
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class NewsFragment(var category: String): Fragment() {
    lateinit var viewBinding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel
    var sourceObj: Source? = null
    var isLoading = false
    var pageSize = 20
    var page = 1

    // try to commit
    // try to send commit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
    }

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
        initObservers()
        viewModel.getNewsSources(category)
//        viewBinding.sideMenuImg.setOnClickListener {
//            toolbar.inflateMenu(R.menu.side_menu)
//
//        }
    }

    private fun initObservers() {
        viewModel.sourceList.observe(viewLifecycleOwner){sources->
            bindTabs(sources)
        }

        viewModel.newsList.observe(viewLifecycleOwner){
            adapter.bindNews(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner){
            handleError(it)
        }
        getNews()

    }

    private fun getNews() {
        viewModel.getNews(sourceObj?.id,pageSize,page)
        isLoading = false
    }

    val adapter = NewsAdapter()
    private fun initView() {
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var layoutManager = recyclerView.layoutManager as LinearLayoutManager
                var lastVisibleItemCount = layoutManager.findLastVisibleItemPosition()
                var totalItemCount = layoutManager.itemCount
                var visibleThreshold = 3
                if (!isLoading&& totalItemCount - lastVisibleItemCount <= visibleThreshold){
                    isLoading = true
                    page++

                }
            }
        })
        adapter.onViewClickListener =
            NewsAdapter.OnViewClickListener { news ->
                val intent = Intent(requireContext(),NewDetailsActivity::class.java)
                intent.putExtra("news",news)
                startActivity(intent)
            }

    }




    private fun handleError(viewError: ViewError){
        showMessage(
            message = viewError.message?:viewError.throwable?.localizedMessage?:"Something went wrong",
            posActionName = "Try Again",
            posAction = { dialogInterface, i ->
                dialogInterface.dismiss()
                viewError.onTryAgainClickListener?.onTryAgainClick()
            },
            negActionName = "Cancel"
        ) { dialogInterface, i ->
            dialogInterface.dismiss()
            val intent = Intent(context,Splash::class.java)
            startActivity(intent)
            activity?.finish()
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
                    sourceObj = source
                    viewModel.getNews(source.id,pageSize,page)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val source = tab?.tag as Source
                    sourceObj = source
                    viewModel.getNews(source.id,pageSize, page)
                }
            }
        )
        viewBinding.tabLayout.getTabAt(0)?.select()
    }
}