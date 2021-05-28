package com.inventiv.gastropaysdk.api

import com.inventiv.gastropaysdk.data.model.response.DummyResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GastroPayService {

    @GET("/posts/{id}")
    suspend fun getDummy(@Path("id") id: Int): DummyResponse
}