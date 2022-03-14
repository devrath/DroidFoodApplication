package com.codinginflow.mvvmnewsapp.data

import com.codinginflow.mvvmnewsapp.api.NewsApi
import javax.inject.Inject

/**
 * map is a operator we can invoke on a list where we can take a item and convert into another and return it
 *
 */
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsArticleDb: NewsArticleDatabase
){

    private val newsArticleDao = newsArticleDb.newsArticleDao()

    suspend fun getBreakingNews() : List<NewsArticle> {
        val response = newsApi.getBreakingNews()
        val serverBreakingNewsArticles = response.articles
        val breakingNewsArticles = serverBreakingNewsArticles.map { serverBreakingNewsArticles ->
            NewsArticle(
                title = serverBreakingNewsArticles.title,
                url = serverBreakingNewsArticles.url,
                thumbnailUrl = serverBreakingNewsArticles.urlToImage,
                isBookmarked = false
            )
        }
        return breakingNewsArticles
    }


}