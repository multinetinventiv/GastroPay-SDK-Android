package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.LastTransactionsResponse
import com.inventiv.gastropaysdk.data.response.TransactionSummaryResponse
import com.inventiv.gastropaysdk.data.response.WalletResponse
import com.inventiv.gastropaysdk.data.safeFlow
import kotlinx.coroutines.flow.Flow

internal class WalletRepositoryImp(private val gastroPayService: GastroPayService) :
    WalletRepository, BaseRepository() {

    override fun wallet(): Flow<Resource<WalletResponse>> {
        return safeFlow {
            gastroPayService.getWallet()
        }
    }

    override fun lastTransactions(
        id: String,
        endTime: String
    ): Flow<Resource<List<LastTransactionsResponse>>> {
        return safeFlow {
            gastroPayService.getLastTransactions(id, endTime)
        }
    }

    override fun transactionSummary(): Flow<Resource<TransactionSummaryResponse>> {
        return safeFlow {
            gastroPayService.getTransactionSummary()
        }
    }
}