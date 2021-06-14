package com.inventiv.gastropaysdk.data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class ApiError(var code: Int, val message: String)

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error(val apiError: ApiError) : Resource<Nothing>()
    class Loading(val isLoading: Boolean) : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}

fun <T> safeFlow(
    modifyFun: ((Resource<T>) -> Resource<T>)? = null,
    suspendFun: suspend () -> T
): Flow<Resource<T>> {
    return flow {
        try {
            val result = suspendFun()
            if (modifyFun != null) {
                val modified = modifyFun(Resource.Success(result))
                emit(modified)
            } else {
                emit(Resource.Success(result))
            }
        } catch (httpE: HttpException) {
            val apiError = ApiError(
                code = httpE.code(),
                message = httpE.message()
            )
            if (modifyFun != null) {
                val modified = modifyFun(Resource.Error(apiError))
                emit(modified)
            } else {
                emit(Resource.Error(apiError))
            }
        } catch (unknownHostE: UnknownHostException) {
            val apiError = ApiError(
                code = -101,
                message = unknownHostE.message.toString()
            )
            if (modifyFun != null) {
                val modified = modifyFun(Resource.Error(apiError))
                emit(modified)
            } else {
                emit(Resource.Error(apiError))
            }
        } catch (socketE: SocketException) {
            val apiError = ApiError(
                code = -102,
                message = socketE.message.toString()
            )
            if (modifyFun != null) {
                val modified = modifyFun(Resource.Error(apiError))
                emit(modified)
            } else {
                emit(Resource.Error(apiError))
            }
        } catch (socketTimeoutE: SocketTimeoutException) {
            val apiError = ApiError(
                code = -103,
                message = socketTimeoutE.message.toString()
            )
            if (modifyFun != null) {
                val modified = modifyFun(Resource.Error(apiError))
                emit(modified)
            } else {
                emit(Resource.Error(apiError))
            }
        } catch (e: Exception) {
            val apiError = ApiError(
                code = -100,
                message = e.message.toString()
            )

        }
    }.onStart {
        emit(Resource.Loading(true))
    }.onCompletion {
        emit(Resource.Loading(false))
    }
}