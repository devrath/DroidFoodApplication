package com.codinginflow.mvvmnewsapp.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codinginflow.mvvmnewsapp.data.NewsArticle
import com.codinginflow.mvvmnewsapp.databinding.ItemNewsArticleBinding

/**
 * ListAdapter has 2 Arguments:
 *     <A> First Argument: Type of data to be shown
 *     <B> Second Argument: View holder we want to use
 *
 * Recycler view just creates just only enough view holders to fill the screen and if needed on the fly it creates now view holders as needed
 *
 * DiffUtilCallback: It is used to compare old data set with new data set
 *        <*> Say a list has old set of items and we pass a new set of items, now we need to know the difference between so that the recycler view can perform appropriate animations
 *        <*> It also makes updating UI more efficient
 *        <*> We pass it to the constructor of ListAdapter
 */
class NewsArticleListAdapter(
    private val onItemClick : (NewsArticle) -> Unit,
    private val onBookmarkClick : (NewsArticle) -> Unit
) : ListAdapter<NewsArticle,NewsArticleViewHolder>(NewsArticleComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val binding =
            ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsArticleViewHolder(
            binding,
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
        // Get the position of the item
        val currentItem = getItem(position)
        // Theoretically current item can return null so we shall check for null and then bind the item
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

}