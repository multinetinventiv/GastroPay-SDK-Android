package com.inventiv.gastropaysdk.ui

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.common.BaseViewModel
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
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

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
}