package com.example.newsapp.NewsApp.ui.News

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.NewsApp.api.model.sourcesResponse.Source
import com.example.newsapp.NewsApp.ui.OnTryAgainClickListener
import com.example.newsapp.NewsApp.ui.Splash
import com.example.newsapp.NewsApp.ui.showMessage
import com.example.newsapp.databinding.FragmentNewsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class NewsFragment(var category: String): Fragment() {
    lateinit var viewBinding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel
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
    }

    private fun initObservers() {
        viewModel.shouldShowLoading.observe(viewLifecycleOwner,object : Observer<Boolean>{
            override fun onChanged(isVisible: Boolean){
                viewBinding.progressBar.isVisible = isVisible
            }
        })
        viewModel.sourceList.observe(viewLifecycleOwner){sources->
            bindTabs(sources)
        }
        viewModel.newsList.observe(viewLifecycleOwner){
            adapter.bindNews(it)

        }

    }

    val adapter = NewsAdapter()
    private fun initView() {
        viewBinding.recyclerView.adapter = adapter
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
            val intent = Intent(context,Splash::class.java)
            startActivity(intent)
            activity?.finish()
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
                    viewModel.getNews(source.id)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val source = tab?.tag as Source
                    viewModel.getNews(source.id)
                }
            }
        )
        viewBinding.tabLayout.getTabAt(0)?.select()
    }
}