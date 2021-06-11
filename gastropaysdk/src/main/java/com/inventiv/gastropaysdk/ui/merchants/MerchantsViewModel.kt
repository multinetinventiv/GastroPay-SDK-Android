package com.inventiv.gastropaysdk.ui.merchants

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.DummyResponse
import com.inventiv.gastropaysdk.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class MerchantsViewModel(val mainRepository: MainRepository) : BaseViewModel() {

    private val _uiState = MutableStateFlow<Resource<DummyResponse>>(Resource.Empty)
    val uiState: StateFlow<Resource<DummyResponse>> get() = _uiState.asStateFlow()

    fun getDummy(id: Int) {
        viewModelScope.launch {
            mainRepository.getDummy(id).collect { response ->
                _uiState.value = response
            }
        }
    }
}