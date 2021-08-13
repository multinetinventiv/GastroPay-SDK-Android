package com.inventiv.gastropaysdk

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inventiv.gastropaysdk.ui.wallet.WalletFragment
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
class WalletFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockWebServer.start(8080)
        mockWebServer.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                with(request.path.replace("http://localhost:8080", "")) {
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
            launchFragmentInContainer<WalletFragment>(
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        val lastTransactionsTitle =
            getResourceString(R.string.wallet_last_transactions_gastropay_sdk)

        onView(withId(R.id.titleTextView))
            .check(matches(withText(lastTransactionsTitle)))
            .check(matches(isDisplayed()))

    }

    @Test
    fun recyclerview_load() {
        val scenario =
            launchFragmentInContainer<WalletFragment>(
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->
            Thread.sleep(500)
        }

        onView(withId(R.id.transactionsRecyclerViewGastroPaySdk))
            .check(
                matches(
                    hasDescendant(withText("Merchant Name Here"))
                )
            )

        onView(withId(R.id.transactionsRecyclerViewGastroPaySdk))
            .check(
                matches(
                    hasDescendant(withText("Merchant Name Here 2"))
                )
            )
    }

    @Test
    fun point_check() {
        val scenario =
            launchFragmentInContainer<WalletFragment>(
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->
            Thread.sleep(500)
        }

        onView(withId(R.id.myTotalPointsTextView))
            .check(matches(withText("801.07")))
            .check(matches(isDisplayed()))
    }
}