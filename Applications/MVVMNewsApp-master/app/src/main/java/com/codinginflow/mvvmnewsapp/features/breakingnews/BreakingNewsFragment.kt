package com.codinginflow.mvvmnewsapp.features.breakingnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.mvvmnewsapp.R
import com.codinginflow.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.codinginflow.mvvmnewsapp.shared.NewsArticleListAdapter
import com.codinginflow.mvvmnewsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


/**
 * When the fragment is detached and attached:: --> Only the view of the fragment is destroyed and recreated but fragment instance remains
 *           <*> launchWhenResumed -> Will pause the collect operation when the fragment instance is not active and resume when it becomes active
 */
@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val viewModel: BreakingNewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBreakingNewsBinding.bind(view)

        val newsArticleAdapter = NewsArticleListAdapter(
            onItemClick = { article ->

            },
            onBookmarkClick = { article ->

            }
        )
        binding.apply {
            recyclerView.apply {
                adapter = newsArticleAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            lifecycleScope.launchWhenResumed {
                viewModel.breakingNews.collect {
                    val result = it?: return@collect

                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    recyclerView.isVisible = !result.data.isNullOrEmpty()

                    textViewError.isVisible = result.error != null && result.data.isNullOrEmpty()
                    buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    textViewError.text = getString(R.string.could_not_refresh,result.error?.localizedMessage?:R.string.unknown_error_occurred)

                    newsArticleAdapter.submitList(result.data)
                }
            }
        }

    }


}