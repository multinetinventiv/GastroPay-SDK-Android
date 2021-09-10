package com.inventiv.gastropaysdk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.request.SearchCriteria
import com.inventiv.gastropaysdk.repository.MainRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class MainViewModel(val mainRepository: MainRepository) : BaseViewModel() {
    sealed class Event {
        object OnBackPressed : Event()
        object CloseSDK : Event()
        data class InitTab(val tabIndex: Int) : Event()
        data class PushFragment(val fragment: BaseFragment) : Event()
        data class PopFragment(val depth: Int) : Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _searchMerchants = MutableLiveData<SearchCriteria>()
    val searchMerchants: LiveData<SearchCriteria> get() = _searchMerchants

    fun searchFilteredMerchants(searchCriteria: SearchCriteria) {
        _searchMerchants.value = searchCriteria
    }

    fun clearSearchFilteredMerchants() {
        _searchMerchants.value = null
    }

    fun closeSdk() {
        viewModelScope.launch {
            eventChannel.send(Event.CloseSDK)
        }
    }

    fun onBackPressed() {
        viewModelScope.launch {
            eventChannel.send(Event.OnBackPressed)
        }
    }

    fun initTab(tabIndex: Int) {
        viewModelScope.launch {
            eventChannel.send(Event.InitTab(tabIndex))
        }
    }

    fun pushFragment(fragment: BaseFragment) {
        viewModelScope.launch {
            eventChannel.send(Event.PushFragment(fragment))
        }
    }

    fun popFragment(depth: Int) {
        viewModelScope.launch {
            eventChannel.send(Event.PopFragment(depth))
        }
    }
}