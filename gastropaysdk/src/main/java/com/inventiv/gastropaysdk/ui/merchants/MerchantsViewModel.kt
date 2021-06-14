package com.inventiv.gastropaysdk.ui.merchants

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.MerchantPaging
import com.inventiv.gastropaysdk.repository.MerchantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class MerchantsViewModel(val merchantRepository: MerchantRepository) : BaseViewModel() {

    private val _uiState = MutableStateFlow<Resource<MerchantPaging>>(Resource.Empty)
    val uiState: StateFlow<Resource<MerchantPaging>> get() = _uiState.asStateFlow()

    fun getMerchants(
        latitude: Double,
        longitude: Double,
        tags: String? = null,
        merchantName: String? = null,
        page: Int = 0
    ) {
        viewModelScope.launch {
            merchantRepository.getMerchants(
                latitude, longitude, tags, merchantName, page
            ).collect { response ->
                _uiState.value = response
            }
        }
    }
}