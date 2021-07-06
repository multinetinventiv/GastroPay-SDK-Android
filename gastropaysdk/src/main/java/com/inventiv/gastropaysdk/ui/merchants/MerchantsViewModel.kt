package com.inventiv.gastropaysdk.ui.merchants

import android.location.Location
import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.response.MerchantsResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.MerchantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class MerchantsViewModel(private val merchantRepository: MerchantRepository) :
    BaseViewModel() {

    private val _uiState = MutableStateFlow<Resource<MerchantsResponse>>(Resource.Empty)
    val uiState: StateFlow<Resource<MerchantsResponse>> get() = _uiState.asStateFlow()

    var currentPage = 0

    fun getMerchants(
        location: Location,
        tags: String? = null,
        merchantName: String? = null
    ) {
        viewModelScope.launch {
            merchantRepository.getMerchants(
                latitude = location.latitude,
                longitude = location.longitude,
                tags = tags,
                merchantName = merchantName,
                page = currentPage
            ).collect { response ->
                _uiState.value = response
            }
        }
    }
}