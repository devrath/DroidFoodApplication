package com.codinginflow.mvvmnewsapp.features.searchnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmnewsapp.R
import com.codinginflow.mvvmnewsapp.databinding.LoadStateFooterBinding

/**
 * In paging 3 library, the load state is represented by its own adapter
 * ** This is a special adapter that has only one or zero items - Depending on whether its loading a new page or not
 */
class NewsArticleLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<NewsArticleLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            LoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    /**
     * We added the view holder here inside because we will not reuse the adapter
     */
    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState is LoadState.Error
                textViewError.isVisible = loadState is LoadState.Error

                if (loadState is LoadState.Error) {
                    textViewError.text = loadState.error.localizedMessage
                        ?: binding.root.context.getString(R.string.unknown_error_occurred)
                }
            }
        }
    }
}