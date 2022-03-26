package com.demo.mvvmnewsapp.shared

import androidx.recyclerview.widget.DiffUtil
import com.demo.mvvmnewsapp.data.NewsArticle

class NewsArticleComparator  : DiffUtil.ItemCallback<NewsArticle>() {

    /**
     * To know if two articles are the same, we can use the url field because url is the primary key
     */
    override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle) =
        oldItem.url == newItem.url

    /**
     * Now in comparing two items, We need to match each value of NewsArticle constructor param
     * So, If either one of the param is different, We return false
     */
    override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle) =
        oldItem == newItem

}