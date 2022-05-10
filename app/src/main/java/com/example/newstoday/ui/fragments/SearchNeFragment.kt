package com.example.newstoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstoday.R
import com.example.newstoday.adapters.NewsAdapter
import com.example.newstoday.ui.NewsViewModel
import com.example.newstoday.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_ne.*
import kotlinx.android.synthetic.main.fragment_search_ne.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNeFragment : Fragment(R.layout.fragment_search_ne) {
    private val viewModel by viewModels<NewsViewModel>()

    lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNeFragment_to_articleFragment,
                bundle
            )
        }

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(3000)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()

                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE

    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
}