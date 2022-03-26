package com.demo.mvvmnewsapp.features.breakingnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.mvvmnewsapp.R
import com.demo.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.demo.mvvmnewsapp.shared.NewsArticleListAdapter
import com.demo.mvvmnewsapp.util.Resource
import com.demo.mvvmnewsapp.util.exhaustive
import com.demo.mvvmnewsapp.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


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
                val uri = Uri.parse(article.url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                requireActivity().startActivity(intent)
            },
            onBookmarkClick = { article ->
                viewModel.onBookmarkClick(article)
            }
        )
        newsArticleAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            recyclerView.apply {
                adapter = newsArticleAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                itemAnimator?.changeDuration = 0
            }
            lifecycleScope.launchWhenResumed {
                viewModel.breakingNews.collect {
                    val result = it?: return@collect

                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    recyclerView.isVisible = !result.data.isNullOrEmpty()

                    textViewError.isVisible = result.error != null && result.data.isNullOrEmpty()
                    buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    textViewError.text = getString(R.string.could_not_refresh,result.error?.localizedMessage?:R.string.unknown_error_occurred)

                    newsArticleAdapter.submitList(result.data) {
                        if(viewModel.pendingScrollToTopAfterRefresh){
                            recyclerView.scrollToPosition(0)
                            viewModel.pendingScrollToTopAfterRefresh = false
                        }
                    }
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
            }
            buttonRetry.setOnClickListener {
                viewModel.onManualRefresh()
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.events.collect { event ->
                    when(event){
                        is BreakingNewsViewModel.Event.ShowErrorMessage ->
                            showSnackbar(
                            message = getString(
                                R.string.could_not_refresh,
                            event.error.localizedMessage
                                ?: getString(R.string.unknown_error_occurred))
                        )
                    }.exhaustive
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_breaking_news,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when(item.itemId) {
            R.id.action_refresh -> {
                viewModel.onManualRefresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
    }

}