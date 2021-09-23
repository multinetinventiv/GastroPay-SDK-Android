package com.inventiv.gastropaysdk.ui.wallet

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.InvoiceSendRequest
import com.inventiv.gastropaysdk.data.response.LastTransactionsResponse
import com.inventiv.gastropaysdk.data.response.TransactionSummaryResponse
import com.inventiv.gastropaysdk.data.response.WalletResponse
import com.inventiv.gastropaysdk.repository.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response

internal class WalletViewModel(private val repository: WalletRepository) :
    BaseViewModel() {

    private var progressCount = 0

    private val _walletState =
        MutableStateFlow<Resource<WalletResponse>>(Resource.Empty)
    val wallet: StateFlow<Resource<WalletResponse>>
        get() = _walletState.asStateFlow()

    private val _lastTransactionsState =
        MutableStateFlow<Resource<List<LastTransactionsResponse>>>(Resource.Empty)
    val lastTransactions: StateFlow<Resource<List<LastTransactionsResponse>>>
        get() = _lastTransactionsState.asStateFlow()

    private val _summaryState =
        MutableStateFlow<Resource<TransactionSummaryResponse>>(Resource.Empty)
    val summary: StateFlow<Resource<TransactionSummaryResponse>>
        get() = _summaryState.asStateFlow()

    private val _invoiceSendState =
        MutableStateFlow<Resource<Response<Unit>>>(Resource.Empty)
    val invoiceSend: StateFlow<Resource<Response<Unit>>>
        get() = _invoiceSendState.asStateFlow()

    fun getLastTransactions(id: String, endTime: String) {
        viewModelScope.launch {
            repository.lastTransactions(id, endTime).collect { response ->
                var transferFlowState = true
                if (response is Resource.Loading) {
                    transferFlowState = prepareProgressVisibilityState(response)
                }
                if (transferFlowState) {
                    _lastTransactionsState.value = response
                }
            }
        }
    }

    fun getWallet() {
        viewModelScope.launch {
            repository.wallet().collect { response ->
                var transferFlowState = true
                if (response is Resource.Loading) {
                    transferFlowState = prepareProgressVisibilityState(response)
                }
                if (transferFlowState) {
                    _walletState.value = response
                }
            }
        }
    }

    fun getSummary() {
        viewModelScope.launch {
            repository.transactionSummary().collect { response ->
                var transferFlowState = true
                if (response is Resource.Loading) {
                    transferFlowState = prepareProgressVisibilityState(response)
                }
                if (transferFlowState) {
                    _summaryState.value = response
                }
            }
        }
    }

    fun sendInvoice(walletTransactionId: Int) {
        viewModelScope.launch {
            val request = InvoiceSendRequest(walletTransactionId)
            repository.invoiceSend(request).collect { response ->
                var transferFlowState = true
                if (response is Resource.Loading) {
                    transferFlowState = prepareProgressVisibilityState(response)
                }
                if (transferFlowState) {
                    _invoiceSendState.value = response
                }
            }
        }
    }

    private fun prepareProgressVisibilityState(resource: Resource.Loading): Boolean {
        progressCount = when {
            resource.isLoading -> {
                progressCount.plus(1)
            }
            progressCount > 0 -> {
                progressCount.minus(1)
            }
            else -> {
                progressCount
            }
        }
        var transferFlowState = true
        if (!resource.isLoading && progressCount > 0) {
            transferFlowState = false
        }
        return transferFlowState
    }
}