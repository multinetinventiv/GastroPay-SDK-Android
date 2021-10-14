package com.inventiv.gastropaysdk.ui

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.SearchCriteria
import com.inventiv.gastropaysdk.repository.MainRepository
import com.inventiv.gastropaysdk.utils.saveSettings
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class MainViewModel(val repository: MainRepository) : BaseViewModel() {

    sealed class HotEvent {
        object OnBackPressed : HotEvent()
        object CloseSDK : HotEvent()
        data class InitTab(val tabIndex: Int) : HotEvent()
        data class PushFragment(val fragment: BaseFragment) : HotEvent()
        data class PopFragment(val depth: Int) : HotEvent()
    }

    sealed class ColdEvent {
        object Empty : ColdEvent()
        data class OnGenericWebViewClick(val isAccept: Boolean) : ColdEvent()
        data class SearchMerchant(val data: SearchCriteria) : ColdEvent()
    }

    private val hotEvent = Channel<HotEvent>(Channel.BUFFERED)
    val hotFlow = hotEvent.receiveAsFlow()

    private val _coldEvent = MutableStateFlow<ColdEvent>(ColdEvent.Empty)
    val coldEvent: StateFlow<ColdEvent> = _coldEvent

    fun searchFilteredMerchants(searchCriteria: SearchCriteria) {
        _coldEvent.value = ColdEvent.SearchMerchant(searchCriteria)
    }

    fun closeSdk() {
        viewModelScope.launch {
            hotEvent.send(HotEvent.CloseSDK)
        }
    }

    fun onBackPressed() {
        viewModelScope.launch {
            hotEvent.send(HotEvent.OnBackPressed)
        }
    }

    fun initTab(tabIndex: Int) {
        viewModelScope.launch {
            hotEvent.send(HotEvent.InitTab(tabIndex))
        }
    }

    fun pushFragment(fragment: BaseFragment) {
        viewModelScope.launch {
            hotEvent.send(HotEvent.PushFragment(fragment))
        }
    }

    fun popFragment(depth: Int) {
        viewModelScope.launch {
            hotEvent.send(HotEvent.PopFragment(depth))
        }
    }

    fun resetColdEvent() {
        viewModelScope.launch {
            _coldEvent.value = ColdEvent.Empty
        }
    }

    fun onGenericWebViewClicked(isAccept: Boolean) {
        viewModelScope.launch {
            _coldEvent.value = ColdEvent.OnGenericWebViewClick(isAccept)
        }
    }

    fun getSettings() {
        viewModelScope.launch {
            repository.getSettings().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data.saveSettings()
                    }
                }
            }
        }
    }
}