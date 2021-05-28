package com.inventiv.gastropaysdk.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.api.response.DummyResponse
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class MainViewModel(val mainRepository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<DummyResponse>>(Resource.Empty)
    val uiState: StateFlow<Resource<DummyResponse>> get() = _uiState


    fun getDummy(id: Int) {
        viewModelScope.launch {
            mainRepository.getDummy(id).collect { response ->
                _uiState.value = response
            }
        }
    }
}