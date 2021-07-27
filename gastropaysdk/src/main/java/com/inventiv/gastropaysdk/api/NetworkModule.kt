package com.inventiv.gastropaysdk.api

import android.util.Log
import com.inventiv.gastropaysdk.data.GastroPayUser
import com.inventiv.gastropaysdk.shared.Environment
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.utils.AUTHORIZATION
import com.inventiv.gastropaysdk.utils.CONNECTION_TIMEOUT_IN_MINUTES
import com.inventiv.gastropaysdk.utils.LIBRARY_GENERAL_LOG_TAG
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal class NetworkModule(private val environment: Environment, private val logging: Boolean) {

    //TODO : set GastropaySdkComponent isUserLoggedIn value based on authToken valid state on Refresh token flow

    private fun provideUserToken(): String {
        return GastroPayUser.authToken ?: ""
    }

    private fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader(AUTHORIZATION, provideUserToken())
                .build()
            chain.proceed(request)
        }
    }

    private fun gastroPayHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            if (message.startsWith("{") || message.startsWith("[")) {
                LogUtils.json(message)
            } else if (GastroPaySdk.getComponent().logging()) {
                Log.d(LIBRARY_GENERAL_LOG_TAG, message)
            }
        }
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = gastroPayHttpLoggingInterceptor()
        loggingInterceptor.level =
            if (logging) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
            .readTimeout(CONNECTION_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
            .addInterceptor(provideAuthInterceptor())
            .addInterceptor(loggingInterceptor)
        return okHttpClientBuilder.build()
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(environment.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }

    fun provideGastroPayService(): GastroPayService {
        return provideRetrofit().create(GastroPayService::class.java)
    }
}