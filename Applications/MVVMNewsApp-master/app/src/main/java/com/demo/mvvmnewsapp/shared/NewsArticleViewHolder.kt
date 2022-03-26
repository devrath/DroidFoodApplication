package com.demo.mvvmnewsapp.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.mvvmnewsapp.R
import com.demo.mvvmnewsapp.data.NewsArticle
import com.demo.mvvmnewsapp.databinding.ItemNewsArticleBinding

class NewsArticleViewHolder(
    // Binding view
    private val binding: ItemNewsArticleBinding,
    // Entire row click
    private val onItemClick : (Int) -> Unit,
    // Bookmark view click
    private val onBookmarkClick : (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        // Position of the adapter


        binding.apply {
            // Set click listener for entire row
            root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
            // Set click listener for a particular item  in the row
            imageViewBookmark.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBookmarkClick(position)
                }
            }
        }
    }

    fun bind(article: NewsArticle) {
        binding.apply {
            Glide.with(itemView)
                .load(article.thumbnailUrl)
                .error(R.drawable.image_placeholder)
                .into(imageView)

            textViewTitle.text = article.title ?: ""

            imageViewBookmark.setImageResource(
                when {
                    article.isBookmarked -> R.drawable.ic_bookmark_selected
                    else -> R.drawable.ic_bookmark_unselected
                }
            )
        }
    }

}