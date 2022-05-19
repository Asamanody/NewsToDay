package com.example.newstoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstoday.R
import com.example.newstoday.adapters.NewsAdapter
import com.example.newstoday.ui.NewsActivity
import com.example.newstoday.ui.NewsViewModel
import com.example.newstoday.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_ne.*

@AndroidEntryPoint
class BreakingNeFragment : Fragment(R.layout.fragment_breaking_ne) {

   // private val viewModel by viewModels<NewsViewModel>()
    lateinit var newsAdapter: NewsAdapter

    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
        setupRecyclerView()

        // pass the article to the next fragment by safe arg
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNeFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())


                    }
                }
                is Resource.Error -> {
                    hideProgressBar()

                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()

                    }
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
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
          //  addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}