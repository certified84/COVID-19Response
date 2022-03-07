package com.certified.covid19response.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.covid19response.R
import com.certified.covid19response.adapter.ArticlesRecyclerAdapter
import com.certified.covid19response.adapter.NewsRecyclerAdapter
import com.certified.covid19response.data.model.News
import com.certified.covid19response.databinding.FragmentHomeBinding
import com.certified.covid19response.util.Config.RAPID_API_KEY
import com.certified.covid19response.util.Extensions.openBrowser
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState
        viewModel.getNews(RAPID_API_KEY)

        binding.apply {
            btnNotifications.setOnClickListener { findNavController().navigate(R.id.notificationsFragment) }

            val articlesAdapter = ArticlesRecyclerAdapter()
            recyclerViewArticles.apply {
                adapter = articlesAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            articlesAdapter.setOnItemClickedListener(object :
                ArticlesRecyclerAdapter.OnItemClickedListener {
                override fun onItemClick(news: News) {
                    requireContext().openBrowser(
                        news.originalUrl,
                        findNavController(),
                        HomeFragmentDirections.actionHomeFragmentToWebFragment(news.originalUrl)
                    )
                }
            })

            val newsAdapter = NewsRecyclerAdapter()
            recyclerViewNews.apply {
                adapter = newsAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                clipChildren = false
                clipToPadding = false
            }

            newsAdapter.setOnItemClickedListener(object :
                NewsRecyclerAdapter.OnItemClickedListener {
                override fun onItemClick(news: News) {
                    requireContext().openBrowser(
                        news.originalUrl,
                        findNavController(),
                        HomeFragmentDirections.actionHomeFragmentToWebFragment(news.originalUrl)
                    )
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<SmoothBottomBar>(R.id.bottomBar).visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}