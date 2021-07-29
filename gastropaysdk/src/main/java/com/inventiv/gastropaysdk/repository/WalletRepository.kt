package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.response.LastTransactionsResponse
import com.inventiv.gastropaysdk.data.response.TransactionSummaryResponse
import com.inventiv.gastropaysdk.data.response.WalletResponse
import com.inventiv.gastropaysdk.model.Resource
import kotlinx.coroutines.flow.Flow

internal interface WalletRepository {

    fun wallet(): Flow<Resource<WalletResponse>>

    fun lastTransactions(
        id: String,
        endTime: String
    ): Flow<Resource<List<LastTransactionsResponse>>>

    fun transactionSummary(): Flow<Resource<TransactionSummaryResponse>>

}