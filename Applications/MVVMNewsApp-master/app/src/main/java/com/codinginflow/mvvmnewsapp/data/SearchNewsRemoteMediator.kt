package com.codinginflow.mvvmnewsapp.data

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.codinginflow.mvvmnewsapp.api.NewsApi
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

private const val NEWS_STARTING_PAGE_INDEX = 1

/**
 * When we use the paging 3 library we can use it in 2 ways with/without offline caching
 * ** RemoteMediator -> This is responsible for loading new pages from the web and saving in database
 */
class SearchNewsRemoteMediator(
    private val searchQuery: String,
    private val newsApi: NewsApi,
    private val newsArticleDb: NewsArticleDatabase,
    private val refreshOnInit: Boolean
) : /**
     * Int : -> This is the first type of argument that is used to identify a page
     * NewsArticle : -> This is the type of data that we want to show
     **/
    RemoteMediator<Int, NewsArticle>() {

    private val newsArticleDao = newsArticleDb.newsArticleDao()
    private val searchQueryRemoteKeyDao = newsArticleDb.searchQueryRemoteKeyDao()

    /**
     * This is a Suspend function: We can perform the tasks mentioned in the background
     * We actually don't call this method the paging library calls this - We just define the logic that needs to be executed
     * Logic can be anything like get data from API and store data in database
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsArticle>
    ): MediatorResult {

        val page = when (loadType) {
            // This defines that data set is cleared and entirely new data is loaded
            LoadType.REFRESH -> NEWS_STARTING_PAGE_INDEX
            // When we reached the end of the result, we inform remote mediator that end of page is reached
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            // This defines that a new page has to be loaded in bottom
            LoadType.APPEND -> searchQueryRemoteKeyDao.getRemoteKey(searchQuery).nextPageKey
        }

        try {
            // Observe the pageSize is defined somewhere else but here we get it from paging-state
            val response = newsApi.searchNews(searchQuery, page, state.config.pageSize)
            // Get the results from the API
            val serverSearchResults = response.articles
            // Get all the bookmark articles
            val bookmarkedArticles = newsArticleDao.getAllBookmarkedArticles().first()

            val searchResultArticles = serverSearchResults.map { serverSearchResultArticle ->
                val isBookmarked = bookmarkedArticles.any { bookmarkedArticle ->
                    bookmarkedArticle.url == serverSearchResultArticle.url
                }

                NewsArticle(
                    title = serverSearchResultArticle.title,
                    url = serverSearchResultArticle.url,
                    thumbnailUrl = serverSearchResultArticle.urlToImage,
                    isBookmarked = isBookmarked
                )
            }

            newsArticleDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // For this type delete all previous data
                    newsArticleDao.deleteSearchResultsForQuery(searchQuery)
                }

                val lastQueryPosition = newsArticleDao.getLastQueryPosition(searchQuery) ?: 0
                var queryPosition = lastQueryPosition + 1

                val searchResults = searchResultArticles.map { article ->
                    SearchResult(searchQuery, article.url, queryPosition++)
                }

                val nextPageKey = page + 1

                newsArticleDao.insertArticles(searchResultArticles)
                newsArticleDao.insertSearchResults(searchResults)
                searchQueryRemoteKeyDao.insertRemoteKey(
                    SearchQueryRemoteKey(searchQuery, nextPageKey)
                )
            }
            return MediatorResult.Success(endOfPaginationReached = serverSearchResults.isEmpty())
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return if (refreshOnInit) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }
}