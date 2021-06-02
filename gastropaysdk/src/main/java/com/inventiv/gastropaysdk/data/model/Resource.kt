package com.inventiv.gastropaysdk.data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.net.UnknownHostException

data class ApiError(val code: Int, val message: String)

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error(val apiError: ApiError) : Resource<Nothing>()
    class Loading(val isLoading: Boolean) : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}

fun <T> safeFlow(suspendfun: suspend () -> T): Flow<Resource<T>> {
    return flow {
        try {
            val result = suspendfun()
            emit(Resource.Success(result))
        } catch (httpE: HttpException) {
            val apiError = ApiError(
                code = httpE.code(),
                message = httpE.message()
            )
            emit(Resource.Error(apiError))
        } catch (unknownHostE: UnknownHostException) {
            val apiError = ApiError(
                code = -1,
                message = unknownHostE.message.toString()
            )
            emit(Resource.Error(apiError))
        } catch (e: Exception) {
            val apiError = ApiError(
                code = -2,
                message = e.message.toString()
            )
            emit(Resource.Error(apiError))
        }
    }.onStart {
        emit(Resource.Loading(true))
    }.onCompletion {
        emit(Resource.Loading(false))
    }
}