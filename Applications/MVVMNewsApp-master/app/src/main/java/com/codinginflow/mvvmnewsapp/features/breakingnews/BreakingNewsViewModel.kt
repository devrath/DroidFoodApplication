package com.codinginflow.mvvmnewsapp.features.breakingnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmnewsapp.data.NewsArticle
import com.codinginflow.mvvmnewsapp.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
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

    /**
     * Let's keep the cold flow in a mutable state flow so that when device is rotated the latest value is returned instead of executing whole flow block again
     * ** We achieve this using --> stateIn
     * ** We indicate that flow becomes active when it is invoked and not in other scenario --> SharingStarted.Lazily
     * ** We pass as null because we want to distinguish from success, loading, error
     * **************************************************************
     * So a StateIn operator turns normal cold flow into hot state flow and retains the latest value
     */
    val breakingNews = repository.getBreakingNews()
        .stateIn(viewModelScope, SharingStarted.Lazily,null)


}