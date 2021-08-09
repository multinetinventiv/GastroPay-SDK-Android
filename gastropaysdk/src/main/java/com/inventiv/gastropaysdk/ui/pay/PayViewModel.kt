package com.inventiv.gastropaysdk.ui.pay

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class PayViewModel(private val repository: PaymentRepository) :
    BaseViewModel() {

    var requestInProgress = false

    private val _provisionInformationState =
        MutableStateFlow<Resource<ProvisionInformationResponse>>(Resource.Empty)
    val provisionInformationState: StateFlow<Resource<ProvisionInformationResponse>>
        get() = _provisionInformationState.asStateFlow()

    fun provisionInformation(qrCode: String) {
        val request = ProvisionInformationRequest(token = qrCode, isBonusUsed = false)
        viewModelScope.launch {
            repository.provisionInformation(request).collect { response ->
                _provisionInformationState.value = response
            }
        }
    }
}