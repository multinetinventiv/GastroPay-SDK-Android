package com.inventiv.gastropaysdk

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SampleViewModelTest {

    /*@get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val viewModel by lazy {
        MainViewModel(MainRepository(NetworkModule.apiService))
    }

    @Before
    fun setUp() =
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks


    @Test
    fun movieDetailFlowMustReturnSuccess() = runBlockingTest {
        // Given
        val detailModel = DummyResponse(userId = 1, id = 1, "", "")

        val mutableStateFlow = MutableStateFlow<Resource<DummyResponse>>(Resource.Empty)

        val myFlow = mutableStateFlow.apply {
            emit(Resource.Loading(true))
            emit(Resource.Success(detailModel))
            emit(Resource.Loading(false))
        }

        // When
        coEvery { viewModel.mainRepository.getDummy(-1) } returns myFlow

        // Then
        val job = launch {
            viewModel.uiState.collectIndexed { index, value ->
                when (index) {
                    0 -> assert(value is Resource.Empty)
                    1 -> assert(value is Resource.Loading)
                    2 -> assert(value is Resource.Success)
                    3 -> assert(value is Resource.Loading)
                }
            }
        }

        viewModel.mainRepository.getDummy(-1)

        job.cancel()
    }*/

}