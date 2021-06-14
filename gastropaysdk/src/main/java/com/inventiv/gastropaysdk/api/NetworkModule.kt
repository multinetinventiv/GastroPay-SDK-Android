package com.inventiv.gastropaysdk.api

import com.inventiv.gastropaysdk.shared.Environment
import com.inventiv.gastropaysdk.utils.CONNECTION_TIMEOUT_IN_MINUTES
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal class NetworkModule(private val environment: Environment, private val logging: Boolean) {

    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (logging) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
            .readTimeout(CONNECTION_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
        return okHttpClientBuilder.build()
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            //TODO : base url environment değerine göre belirleniyor olacak
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }

    fun provideGastroPayService(): GastroPayService {
        return provideRetrofit().create(GastroPayService::class.java)
    }
}