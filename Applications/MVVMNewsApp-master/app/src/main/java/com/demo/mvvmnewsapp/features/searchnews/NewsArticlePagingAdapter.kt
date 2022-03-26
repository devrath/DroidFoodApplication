package com.demo.mvvmnewsapp.features.searchnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.demo.mvvmnewsapp.data.NewsArticle
import com.demo.mvvmnewsapp.databinding.ItemNewsArticleBinding
import com.demo.mvvmnewsapp.shared.NewsArticleComparator
import com.demo.mvvmnewsapp.shared.NewsArticleViewHolder

/**
 * PagingDataAdapter: This is a class from paging 3 library
 * *
 * PagingDataAdapter and the LoadStateAdapter are combined together by the library by a concat adapter to show both adapters in the recycler view
 *
 */
class NewsArticlePagingAdapter(
    private val onItemClick: (NewsArticle) -> Unit,
    private val onBookmarkClick: (NewsArticle) -> Unit
) : PagingDataAdapter<NewsArticle, NewsArticleViewHolder>(NewsArticleComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val binding =
            ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsArticleViewHolder(binding,
            onItemClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onItemClick(article)
                }
            },
            onBookmarkClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onBookmarkClick(article)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}