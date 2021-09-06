package com.inventiv.gastropaysdk.ui.merchants.searchmerchant

import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.repository.MerchantRepository

internal class SearchMerchantViewModel(private val merchantRepository: MerchantRepository) :
    BaseViewModel() {

    /*private val _uiState = MutableStateFlow<Resource<MerchantDetailResponse>>(Resource.Empty)
    val uiState: StateFlow<Resource<MerchantDetailResponse>> get() = _uiState.asStateFlow()

    fun getMerchantDetail(id: String) {
        viewModelScope.launch {
            merchantRepository.getMerchantDetail(id = id).collect { response ->
                _uiState.value = response
            }
        }
    }*/
}