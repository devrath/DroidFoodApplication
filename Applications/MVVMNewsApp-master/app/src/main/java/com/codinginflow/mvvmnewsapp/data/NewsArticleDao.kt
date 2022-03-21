package com.codinginflow.mvvmnewsapp.data

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * OnConflictStrategy.REPLACE : ---> If there is an article with this url as the primary already exists, it will be replaced
 * suspend : ---> Suspend helps us to pause and resume the code and will shift the work from a UI thread
 *
 * FLOW : ---> We get a asynchronous stream of data, So we don't need a suspend modifier
 *        ---> Also important point to note here is that when the data is updated the flow is automatically emitted
 *
 */

@Dao
interface NewsArticleDao {

    @Query("SELECT * FROM breaking_news INNER JOIN news_articles ON articleUrl = url")
    fun getAllBreakingNewsArticles() : Flow<List<NewsArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles : List<NewsArticle>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreakingNews(articles : List<BreakingNews>)

    @Query("DELETE FROM breaking_news")
    suspend fun deleteAllBreakingNews()

    @Query("DELETE FROM news_articles WHERE updatedAt < :timeStampInMillis AND isBookmarked=0")
    suspend fun deleteNonBookMarkedArticlesOlderThan(timeStampInMillis : Long)

    @Update
    suspend fun updateArticle(article: NewsArticle)
}