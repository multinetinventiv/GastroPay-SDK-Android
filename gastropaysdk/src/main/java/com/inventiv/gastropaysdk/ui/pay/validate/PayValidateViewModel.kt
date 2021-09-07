package com.inventiv.gastropaysdk.ui.pay.validate

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response

internal class PayValidateViewModel(private val repository: PaymentRepository) :
    BaseViewModel() {

    private val _bankCardsState = MutableStateFlow<Resource<List<BankCardResponse>>>(Resource.Empty)
    val bankCardsState: StateFlow<Resource<List<BankCardResponse>>>
        get() = _bankCardsState.asStateFlow()

    private val _provisionInformationState =
        MutableStateFlow<Resource<ProvisionInformationResponse>>(Resource.Empty)
    val provisionInformationState: StateFlow<Resource<ProvisionInformationResponse>>
        get() = _provisionInformationState.asStateFlow()

    private val _confirmProvisionState = MutableStateFlow<Resource<Response<Unit>>>(Resource.Empty)
    val confirmProvisionState: StateFlow<Resource<Response<Unit>>>
        get() = _confirmProvisionState.asStateFlow()

    fun getBankCards() {
        viewModelScope.launch {
            repository.bankCards().collect { response ->
                _bankCardsState.value = response
            }
        }
    }

    fun provisionInformation(qrCode: String, isBonusUsed: Boolean) {
        val request = ProvisionInformationRequest(token = qrCode, isBonusUsed = isBonusUsed)
        viewModelScope.launch {
            repository.provisionInformation(request).collect { response ->
                _provisionInformationState.value = response
            }
        }
    }

    fun confirmProvision(request: ConfirmProvisionRequest) {
        viewModelScope.launch {
            repository.confirmProvision(request).collect { response ->
                _confirmProvisionState.value = response
            }
        }
    }
}