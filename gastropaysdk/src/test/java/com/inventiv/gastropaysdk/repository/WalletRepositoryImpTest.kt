package com.inventiv.gastropaysdk.repository

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.AmountModel
import com.inventiv.gastropaysdk.data.response.LastTransactionsResponse
import com.inventiv.gastropaysdk.data.response.TransactionSummaryResponse
import com.inventiv.gastropaysdk.data.response.WalletResponse
import com.inventiv.gastropaysdk.utils.ApiUtil
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class WalletRepositoryImpTest {

    private val service: GastroPayService = mock()

    private val repository = WalletRepositoryImp(service)

    @Test
    fun `wallet repository success test`() = runBlocking {
        // Given
        val expected = WalletResponse(
            "123456"
        )

        // When
        whenever(service.getWallet()).thenReturn(expected)

        // Then
        repository.wallet().collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data).isEqualTo(expected)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `wallet repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = WalletResponse::class.java,
            code = 400
        )

        // When
        whenever(service.getWallet()).thenThrow(expected)

        // Then
        repository.wallet().collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Error)
                val error = response as Resource.Error
                Truth.assertThat(error.apiError.code).isEqualTo(400)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }


    @Test
    fun `getLastTransactions repository success test`() = runBlocking {
        // Given
        val expected = listOf(
            LastTransactionsResponse(
                id = 1,
                merchantName = "merchantName",
                transactionAmount = AmountModel(1.0, "", "", ""),
                walletTransactionType = 10,
                transactionDate = 1627560452,
                invoiceNumber = null
            )
        )

        // When
        whenever(service.getLastTransactions("1", "1")).thenReturn(expected)

        // Then
        repository.lastTransactions("1", "1").collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data).isEqualTo(expected)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `getLastTransactions repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = LastTransactionsResponse::class.java,
            code = 400
        )

        // When
        whenever(service.getLastTransactions("1", "1")).thenThrow(expected)

        // Then
        repository.lastTransactions("1", "1").collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Error)
                val error = response as Resource.Error
                Truth.assertThat(error.apiError.code).isEqualTo(400)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `getTransactionSummary repository success test`() = runBlocking {
        // Given
        val expected = TransactionSummaryResponse(
            AmountModel(
                1.0,
                "",
                "",
                ""
            )
        )

        // When
        whenever(service.getTransactionSummary()).thenReturn(expected)

        // Then
        repository.transactionSummary().collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data).isEqualTo(expected)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `transactionSummary repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = TransactionSummaryResponse::class.java,
            code = 400
        )

        // When
        whenever(service.getTransactionSummary()).thenThrow(expected)

        // Then
        repository.transactionSummary().collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Error)
                val error = response as Resource.Error
                Truth.assertThat(error.apiError.code).isEqualTo(400)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }
}