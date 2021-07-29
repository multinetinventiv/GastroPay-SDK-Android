package com.inventiv.gastropaysdk.ui.wallet

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.response.LastTransactionsResponse
import com.inventiv.gastropaysdk.data.response.TransactionSummaryResponse
import com.inventiv.gastropaysdk.data.response.WalletResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class WalletViewModel(private val repository: WalletRepository) :
    BaseViewModel() {

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

    fun lastTransactions(id: String, endTime: String) {
        viewModelScope.launch {
            repository.lastTransactions(id, endTime).collect { response ->
                _lastTransactionsState.value = response
            }
        }
    }

    fun getWallet() {
        viewModelScope.launch {
            repository.wallet().collect { response ->
                _walletState.value = response
            }
        }
    }

    fun getSummary() {
        viewModelScope.launch {
            repository.transactionSummary().collect { response ->
                _summaryState.value = response
            }
        }
    }
}