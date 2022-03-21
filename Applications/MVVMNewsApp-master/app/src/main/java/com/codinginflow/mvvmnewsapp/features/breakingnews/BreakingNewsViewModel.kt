package com.codinginflow.mvvmnewsapp.features.breakingnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmnewsapp.data.NewsArticle
import com.codinginflow.mvvmnewsapp.data.NewsRepository
import com.codinginflow.mvvmnewsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
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

    init {
        viewModelScope.launch {
            repository.deleteNonBookmarkedArticlesOlderThan(
                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
            )
        }
    }

    /**
     * Fragment should not be able to add values into the channel instead it should only be able to take value from the channel
     * Turning into the flow, the fragment can't put anything into it
     */
    private val _eventChannel = Channel<Event>()
    val events = _eventChannel.receiveAsFlow()

    var pendingScrollToTopAfterRefresh = false

    /**
     * We use channel to communicate between the coroutines
     * * One coroutine can put something into the channel and other coroutine can take out something out of the channel.
     * * Sender can suspend if the receiver is not ready yet and is processing the old values
     * * Receiver can suspend if there are no values present in channel
     * * We can turn the channel later in to a flow for usage
     */
    private val refreshTriggerChannel = Channel<Refresh>()
    // Unlike normal flow once it is sent and then it is gone : -> Also called as hot data flow since its sends data even if there is collector or not
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    /**
     * Let's keep the cold flow in a mutable state flow so that when device is rotated the latest value is returned instead of executing whole flow block again
     * ** We achieve this using --> stateIn
     * ** We indicate that flow becomes active when it is invoked and not in other scenario --> SharingStarted.Lazily
     * ** We pass as null because we want to distinguish from success, loading, error
     * **************************************************************
     * So a StateIn operator turns normal cold flow into hot state flow and retains the latest value
     * *
     * flatMapLatest -> Latest means it always cancels old one and gives new one when called
     */
    val breakingNews = refreshTrigger.flatMapLatest { refresh ->
        repository.getBreakingNews(
            refresh == Refresh.FORCE,
            onFetchSuccess = {
                pendingScrollToTopAfterRefresh = true
            },
            onFetchFailed  = { t ->
                viewModelScope.launch {
                    _eventChannel.send(Event.ShowErrorMessage(t))
                }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily,null)


    /**
     * This will be called when we execute swipe refresh
     * ***
     * Point to remember:: If we just re-assign the old-reference with the data of new reference
     * the collect wont work because collect was earlier called with older reference
     * * So we need to keep our old flow and switch its stream to a new flow -> We can do this using flatMap latest operator
     * Basically we use another flow and with that we trigger the existing one
     * * We cannot use a state flow because it ignores a value if the new value is same as existing one
     */
    fun onManualRefresh() {
        viewModelScope.launch {
            // If we are already loading, we should not be refreshing immediately until loading is completed
            if(breakingNews.value !is Resource.Loading){
                // It doesn't carry any data, It is just a signal
                refreshTriggerChannel.send(Refresh.FORCE)
            }
        }
    }

    /**
     * We shall call it when our fragments become active
     */
    fun onStart() {
        viewModelScope.launch {
            // If we are already loading, we should not be refreshing immediately until loading is completed
            if(breakingNews.value !is Resource.Loading){
                // It doesn't carry any data, It is just a signal
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        }
    }

    /**
     * Enum is a limited set of values, it can take only the values that we define inside the body
     */
    enum class Refresh { FORCE , NORMAL }

    /**
     * Difference between the sealed class and enum is that enum cannot hold data but sealed class can hold data
     */
    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
    }

}