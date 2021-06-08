package com.inventiv.gastropaysdk

import com.google.common.truth.Truth.assertThat
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.DummyResponse
import com.inventiv.gastropaysdk.repository.MainRepository
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MainRepositoryTest {

    private val repository: MainRepository = mock()

    @Test
    fun `main repository success test`() = runBlocking {
        // Given
        val expected = DummyResponse(1, 1, "", "")

        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(expected))
            emit(Resource.Loading(false))
        }

        // When
        whenever(repository.getDummy(1)).thenReturn(flow)

        // Then
        repository.getDummy(1).collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                assertThat(response is Resource.Success)
                val success = response as Resource.Success
                assertThat(success.data).isEqualTo(expected)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }

    }
}
