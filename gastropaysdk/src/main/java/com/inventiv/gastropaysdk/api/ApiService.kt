package com.inventiv.gastropaysdk.api

import com.inventiv.gastropaysdk.api.response.DummyResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface ApiService {

    @GET("/posts/{id}")
    suspend fun getDummy(@Path("id") id: Int): DummyResponse
}