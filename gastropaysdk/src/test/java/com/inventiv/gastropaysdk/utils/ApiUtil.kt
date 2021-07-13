package com.inventiv.gastropaysdk.utils

import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

object ApiUtil {

    private const val dummyErrorContent = """ {
            "resultCode": 20201,
            "resultMessage": ERR-24903,
            "errorLink": https:\/\/docs.inventiv.com.tr\/gateway\/errors\/20201.html
        } """

    fun <T> dummyHttpException(
        response: Class<T>,
        code: Int = 400,
        errorContent: String? = null
    ): HttpException {
        val dummyErrorResponse = Response.error<T>(
            code,
            ResponseBody.create(
                MediaType.parse(errorContent ?: dummyErrorContent),
                "application/json; charset=utf-8"
            )
        )
        return HttpException(dummyErrorResponse)
    }
}