package com.inventiv.gastropaysdk

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.repository.MainRepository
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.utils.MainCoroutineScopeRule
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val mainRepository: MainRepository = mock()

    private val viewModel by lazy {
        MainViewModel(mainRepository)
    }

    @Test
    fun `main viewmodel success test`() = runBlockingTest {
        // Given
        val expected = DummyResponse(1, 1, "", "")
        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(expected))
            emit(Resource.Loading(false))
        }

        // When
        whenever(mainRepository.getDummy(1)).thenReturn(flow)

        val job = launch {
            viewModel.uiState.collectIndexed { index, response ->
                when (index) {
                    0 -> {
                        assert(response is Resource.Empty)
                    }
                    1 -> {
                        response.loadingTrueExpected()
                    }
                    2 -> {
                        Truth.assertThat(response is Resource.Success)
                        val success = response as Resource.Success
                        Truth.assertThat(success.data).isEqualTo(expected)
                    }
                    3 -> {
                        response.loadingFalseExpected()
                    }
                }
            }
        }

        viewModel.getDummy(1)
        job.cancel()
    }

    /* TODO Turbine
    @Test
    fun `main viewmodel success test with turbine`() = runBlockingTest {
        // Given
        val detailModel = DummyResponse(1, 1, "", "")
        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(detailModel))
            emit(Resource.Loading(false))
        }

        // When
        whenever(mainRepository.getDummy(1)).thenReturn(flow)

        viewModel.uiState.test {
            viewModel.getDummy(-1)

            assert(expectItem() is Resource.Empty)
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Success)
            assert(expectItem() is Resource.Loading)

            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

    }
     */


}