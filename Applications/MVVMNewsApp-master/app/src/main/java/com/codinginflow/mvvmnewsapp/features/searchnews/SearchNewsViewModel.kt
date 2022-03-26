package com.codinginflow.mvvmnewsapp.features.searchnews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.codinginflow.mvvmnewsapp.data.NewsArticle
import com.codinginflow.mvvmnewsapp.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    state: SavedStateHandle
) : ViewModel() {

    // This will hold the current search query
    private val currentQuery = state.getLiveData<String?>("currentQuery", null)

    val hasCurrentQuery = currentQuery.asFlow().map { it != null }

    private var refreshOnInit = false

    /**
     * We want to emit a new flow when the current query string emits a new query string
     * Here will will not be able to search for the query 2 times in a row
     * CachedIn lets by not loading the new pages when app goes to background or a different page
     */
    val searchResults = currentQuery.asFlow().flatMapLatest { query ->
        query?.let {
            repository.getSearchResultsPaged(query, refreshOnInit)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    var refreshInProgress = false
    var pendingScrollToTopAfterRefresh = false

    var newQueryInProgress = false
    var pendingScrollToTopAfterNewQuery = false

    fun onSearchQuerySubmit(query: String) {
        refreshOnInit = true
        // Set the current query
        currentQuery.value = query
        newQueryInProgress = true
        pendingScrollToTopAfterNewQuery = true
    }

    fun onBookmarkClick(article: NewsArticle) {
        val currentlyBookmarked = article.isBookmarked
        val updatedArticle = article.copy(isBookmarked = !currentlyBookmarked)
        viewModelScope.launch {
            repository.updateArticle(updatedArticle)
        }
    }
}