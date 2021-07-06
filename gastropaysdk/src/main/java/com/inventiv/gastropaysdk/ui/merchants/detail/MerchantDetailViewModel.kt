package com.inventiv.gastropaysdk.ui.merchants.detail

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.response.MerchantDetailResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.MerchantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class MerchantDetailViewModel(private val merchantRepository: MerchantRepository) :
    BaseViewModel() {

    private val _uiState = MutableStateFlow<Resource<MerchantDetailResponse>>(Resource.Empty)
    val uiState: StateFlow<Resource<MerchantDetailResponse>> get() = _uiState.asStateFlow()

    fun getMerchantDetail(id: String) {
        viewModelScope.launch {
            merchantRepository.getMerchantDetail(id = id).collect { response ->
                _uiState.value = response
            }
        }
    }
}