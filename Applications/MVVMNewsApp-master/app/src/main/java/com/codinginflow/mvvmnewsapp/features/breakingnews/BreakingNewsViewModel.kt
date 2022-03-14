package com.codinginflow.mvvmnewsapp.features.breakingnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmnewsapp.data.NewsArticle
import com.codinginflow.mvvmnewsapp.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Co-Routine:: ->
 *             <*> A block of code that can be paused and resumed later on
 *             <*> We can make a concurrent request run like a sequentially without freezing the UI
 * ***************
 * Role of reactive data source:: ->
 *             <*> Since the fragments observe the data from the view model there is no direct reference of data ket in the fragment.
 *             <*> So this prevents memory leaks in a possibility where the fragment is destroyed and VM keeps the reference of the data in fragment.
 */

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val breakingNewFlow = MutableStateFlow<List<NewsArticle>>(emptyList())
    val breakingNews : Flow<List<NewsArticle>> = breakingNewFlow

    init {
        viewModelScope.launch {
            val news = repository.getBreakingNews()
            breakingNewFlow.value = news
        }
    }

}