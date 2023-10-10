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
import com.example.newsapp.R
import com.example.newsapp.newsApp.api.model.sourcesResponse.Source
import com.example.newsapp.newsApp.ui.Splash
import com.example.newsapp.newsApp.ui.ViewError
import com.example.newsapp.newsApp.ui.showMessage
import com.example.newsapp.databinding.FragmentNewsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class NewsFragment(var category: String): Fragment() {
    lateinit var viewBinding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel

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

    }

    val adapter = NewsAdapter()
    private fun initView() {
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewBinding.recyclerView.adapter = adapter
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