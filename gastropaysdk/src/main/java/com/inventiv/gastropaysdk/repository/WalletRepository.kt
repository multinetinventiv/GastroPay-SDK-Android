package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.InvoiceSendRequest
import com.inventiv.gastropaysdk.data.response.LastTransactionsResponse
import com.inventiv.gastropaysdk.data.response.TransactionSummaryResponse
import com.inventiv.gastropaysdk.data.response.WalletResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

internal interface WalletRepository {

    fun wallet(): Flow<Resource<WalletResponse>>

    fun lastTransactions(
        id: String,
        endTime: String
    ): Flow<Resource<List<LastTransactionsResponse>>>

    fun transactionSummary(): Flow<Resource<TransactionSummaryResponse>>

    fun invoiceSend(request: InvoiceSendRequest): Flow<Resource<Response<Unit>>>

}