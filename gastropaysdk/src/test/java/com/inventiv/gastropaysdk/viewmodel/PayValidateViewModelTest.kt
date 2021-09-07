package com.inventiv.gastropaysdk.viewmodel

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.request.TokenType
import com.inventiv.gastropaysdk.data.response.AmountModel
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.repository.PaymentRepositoryImp
import com.inventiv.gastropaysdk.ui.pay.validate.PayValidateViewModel
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
import retrofit2.Response
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class PayValidateViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private lateinit var viewModel: PayValidateViewModel

    private var service: GastroPayService = mock()

    private val repository = PaymentRepositoryImp(service)

    @Before
    fun setUp() {
        viewModel = PayValidateViewModel(repository)
    }

    //region provisionInformation
    @Test
    fun `provisionInformation viewmodel success test`() = runBlockingTest {

        // Given
        val expected = ProvisionInformationResponse(
            "1",
            "",
            AmountModel(0.0, "", "", ""),
            null,
            null,
            AmountModel(0.0, "", "", ""),
            AmountModel(0.0, "", "", ""),
            AmountModel(0.0, "", "", ""),
            "",
            null,
            null,
        )

        val request = ProvisionInformationRequest(
            token = "123456",
            isBonusUsed = false,
            tokenType = TokenType.QR.value
        )

        // When
        whenever(service.provisionInformation(request)).thenReturn(expected)

        launch {
            val list: List<Resource<ProvisionInformationResponse>> =
                viewModel.provisionInformationState.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.provisionInformation("123456", false)
    }
    //endregion provisionInformation

    //region confirmProvision
    @Test
    fun `confirmProvision viewmodel success test`() = runBlockingTest {

        // Given
        val expected = Response.success(Unit)

        val request = ConfirmProvisionRequest(
            token = "",
            cardId = 1,
            pinCode = null,
            useCashback = false
        )

        // When
        whenever(service.confirmProvision(request)).thenReturn(expected)

        launch {
            val list: List<Resource<Response<Unit>>> =
                viewModel.confirmProvisionState.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.confirmProvision(request)
    }
    //endregion confirmProvision

    //region getBankCards
    @Test
    fun `getBankCards viewmodel success test`() = runBlockingTest {

        // Given
        val expected = listOf(
            BankCardResponse(
                id = 1,
                number = "12345678",
                alias = "",
                binNumber = "",
                isDefault = true,
                bankImageUrl = null,
                paymentSourceImageUrl = null
            ),
            BankCardResponse(
                id = 2,
                number = "987654321",
                alias = "",
                binNumber = "",
                isDefault = false,
                bankImageUrl = null,
                paymentSourceImageUrl = null
            )
        )

        // When
        whenever(service.getBankCards()).thenReturn(expected)

        launch {
            val list: List<Resource<List<BankCardResponse>>> =
                viewModel.bankCardsState.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)
            Truth.assertThat(response.data[0].isDefault).isEqualTo(true)
            Truth.assertThat(response.data[1].isDefault).isEqualTo(false)

            list[3].loadingFalseExpected()
        }

        viewModel.getBankCards()
    }
    //endregion getBankCards
}