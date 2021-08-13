package com.inventiv.gastropaysdk.viewmodel

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.response.AmountModel
import com.inventiv.gastropaysdk.data.response.LastTransactionsResponse
import com.inventiv.gastropaysdk.data.response.TransactionSummaryResponse
import com.inventiv.gastropaysdk.data.response.WalletResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.WalletRepositoryImp
import com.inventiv.gastropaysdk.ui.wallet.WalletViewModel
import com.inventiv.gastropaysdk.utils.MainCoroutineScopeRule
import com.inventiv.gastropaysdk.utils.emptyExpected
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class WalletViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private lateinit var viewModel: WalletViewModel

    private var service: GastroPayService = mock()

    private val repository = WalletRepositoryImp(service)

    @Before
    fun setUp() {
        viewModel = WalletViewModel(repository)
    }

    @Test
    fun `wallet viewmodel wallet success test`() = runBlockingTest {

        val expected = WalletResponse(
            "123456"
        )

        // When
        whenever(service.getWallet()).thenReturn(expected)

        launch {
            val list: List<Resource<WalletResponse>> = viewModel.wallet.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.getWallet()
    }

    @Test
    fun `wallet viewmodel transactionSummary success test`() = runBlockingTest {

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

        launch {
            val list: List<Resource<TransactionSummaryResponse>> =
                viewModel.summary.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.getSummary()
    }

    @Test
    fun `wallet viewmodel lastTransactions success test`() = runBlockingTest {

        val expected = listOf(
            LastTransactionsResponse(
                1,
                "",
                AmountModel(1.0, "", "", ""),
                1L
            )
        )

        // When
        whenever(service.getLastTransactions("1", "1")).thenReturn(expected)

        launch {
            val list: List<Resource<List<LastTransactionsResponse>>> =
                viewModel.lastTransactions.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.getLastTransactions("1", "1")
    }
}