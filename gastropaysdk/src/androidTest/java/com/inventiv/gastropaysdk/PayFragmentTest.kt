package com.inventiv.gastropaysdk

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inventiv.gastropaysdk.ui.pay.PayFragment
import com.inventiv.gastropaysdk.utils.getResourceString
import com.inventiv.gastropaysdk.utils.readTestResourceFile
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PayFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockWebServer.start(8080)
        mockWebServer.dispatcher = (object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                with(request.path!!.replace("http://localhost:8080", "")) {
                    val response = MockResponse().setResponseCode(200)
                    if (startsWith("/wallet/transactions/")) { //2
                        return response.setBody(readTestResourceFile("wallet-transactions-200.json"))
                    }
                    return when (this) {
                        "/wallet/wallets" -> {  //1
                            response.setBody(readTestResourceFile("wallet-200.json"))
                        }
                        "/wallet/transaction_summary" -> { //3
                            response.setBody(readTestResourceFile("wallet-transaction_summary-200.json"))
                        }
                        else -> response
                    }
                }
            }
        })
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun view_titles_check() {
        val scenario =
            launchFragmentInContainer<PayFragment>(
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        val title =
            getResourceString(R.string.qr_scan_qrcode_gastropay_sdk)

        Espresso.onView(ViewMatchers.withId(R.id.textTitleGastroPaySdk))
            .check(ViewAssertions.matches(ViewMatchers.withText(title)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}