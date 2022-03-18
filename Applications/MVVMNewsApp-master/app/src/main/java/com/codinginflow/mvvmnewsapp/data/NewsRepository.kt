package com.codinginflow.mvvmnewsapp.data

import androidx.room.withTransaction
import com.codinginflow.mvvmnewsapp.api.NewsApi
import com.codinginflow.mvvmnewsapp.util.Resource
import com.codinginflow.mvvmnewsapp.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
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

    /**
     * We have not added the suspend for the function because: ->
     *   <*> Returning a flow will not need suspend function
     *   <*> Collecting a flow is a suspend function
     * ******************************
     * This is a cold flow: -> This will be executed only when it is called and not in other scenario
     */
    fun getBreakingNews() : Flow<Resource<List<NewsArticle>>> =
        /**
         * NetworkBoundResource function that decides to return or fetch new data
         * Good thing about network bound resource is that it is reusable, in a different place its re-usable
         */
        networkBoundResource(
            query = {
                /**
                 * This function is supposed to return the flow of articles from the database
                 * ---> This also gives new data when something changes in the database
                 */
                newsArticleDao.getAllBreakingNewsArticles()
            },
            fetch = {
                /**
                 * Get data from the web service
                 */
                val resource = newsApi.getBreakingNews()
                resource.articles
            },
            saveFetchResult = { serverBreakingNewsArticles ->
                /**
                 * This method takes data from fetch and adds it into the database
                 */
                val breakingNewsArticles = serverBreakingNewsArticles.map { serverBreakingNewsArticle ->
                    NewsArticle(
                        title = serverBreakingNewsArticle.title,
                        url = serverBreakingNewsArticle.url,
                        thumbnailUrl = serverBreakingNewsArticle.urlToImage,
                        isBookmarked = false
                    )
                }

                val breakingNews = breakingNewsArticles.map { article ->
                    BreakingNews(
                        articleUrl = article.url
                    )
                }

                /**
                 * We shall use a database transaction:
                 *    ---> Using the database transaction, either all the transactions should be done or none should be done
                 */
                newsArticleDb.withTransaction {
                    newsArticleDao.deleteAllBreakingNews()
                    newsArticleDao.insertArticles(breakingNewsArticles)
                    newsArticleDao.insertBreakingNews(breakingNews)
                }
            }
        )


}