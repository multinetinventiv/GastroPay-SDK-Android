package com.inventiv.gastropaysdk.merchants

import android.location.Location
import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.data.response.MerchantsResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.MerchantRepository
import com.inventiv.gastropaysdk.ui.merchants.MerchantsViewModel
import com.inventiv.gastropaysdk.utils.MainCoroutineScopeRule
import com.inventiv.gastropaysdk.utils.emptyExpected
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class MerchantsViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private val repository: MerchantRepository = mock()

    private var viewModel: MerchantsViewModel = mock()

    private var location: Location = mock()

    @Before
    fun setUp() {
        viewModel = MerchantsViewModel(repository)
    }

    @Test
    fun `main viewmodel success test`() = runBlockingTest {

        val expected = MerchantsResponse(
            merchants = listOf(),
            isLastPage = true
        )
        // Given
        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(expected))
            emit(Resource.Loading(false))
        }

        // When
        whenever(repository.getMerchants(0.0, 0.0, null, null, 0)).thenReturn(flow)

        launch {
            val list: List<Resource<MerchantsResponse>> = viewModel.uiState.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        location.latitude = 0.0
        location.longitude = 0.0

        viewModel.getMerchants(location)
    }

}