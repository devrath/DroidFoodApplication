package com.codinginflow.mvvmnewsapp.data

import androidx.room.withTransaction
import com.codinginflow.mvvmnewsapp.api.NewsApi
import com.codinginflow.mvvmnewsapp.util.Resource
import com.codinginflow.mvvmnewsapp.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
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
    fun getBreakingNews(
        forceRefresh : Boolean,
        onFetchFailed : (Throwable) -> Unit,
        onFetchSuccess : () -> Unit
    ) : Flow<Resource<List<NewsArticle>>> =
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
            },
            shouldFetch = { cachedArticles ->
                if(forceRefresh){
                    // This is from the functionality of swipe to refresh
                    true
                }else{
                    /**
                     * This is used to check if the data set is too old and then fetch new data set from the web
                     */
                    val sortedArticles = cachedArticles.sortedBy { article ->
                        article.updatedAt
                    }
                    val oldestTimeStamp = sortedArticles.firstOrNull()?.updatedAt
                    val needsRefresh = oldestTimeStamp == null
                            || oldestTimeStamp < System.currentTimeMillis() -
                            TimeUnit.MINUTES.toMillis(5)
                    needsRefresh
                }
            },
            onFetchFailed = { t ->
                // Transfer the control back to the view model
                if (t !is HttpException && t !is IOException) {
                    // This is because we are handling only http and io exception else we crash the app
                    throw t
                }
                onFetchFailed(t)
            },
            onFetchSuccess = onFetchSuccess

        )

    suspend fun deleteNonBookmarkedArticlesOlderThan(timeStampInMillis:Long){
        newsArticleDao.deleteNonBookMarkedArticlesOlderThan(timeStampInMillis)
    }

    suspend fun updateArticle(article: NewsArticle) {
        newsArticleDao.updateArticle(article)
    }

}