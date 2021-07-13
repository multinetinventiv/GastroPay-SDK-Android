package com.inventiv.gastropaysdk.utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

internal fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
    enqueue(
        MockResponse()
            .setResponseCode(code)
            .setBody(readTestResourceFile(fileName))
    )
}

internal fun readTestResourceFile(fileName: String): String {
    val testContext: Context = InstrumentationRegistry.getInstrumentation().context
    val inputStream = testContext.assets.open("api-response/$fileName")
    val builder = StringBuilder()
    InputStreamReader(inputStream, StandardCharsets.UTF_8).readLines().forEach {
        builder.append(it)
    }
    return builder.toString()
}

internal fun dispatcherWithCustomBody() = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        with(request.path.replace("http://localhost:8080", "")) {
            val response = MockResponse().setResponseCode(200)
            return when (this) {
                "/auth/login" -> {
                    response.setBody(readTestResourceFile("login-200.json"))
                }
                else -> response
            }
        }
    }
}

fun getResourceString(id: Int): String {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    return appContext.resources.getString(id)
}