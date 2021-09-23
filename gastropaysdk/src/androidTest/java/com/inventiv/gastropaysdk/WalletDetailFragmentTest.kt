package com.inventiv.gastropaysdk

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inventiv.gastropaysdk.data.TransactionModel
import com.inventiv.gastropaysdk.ui.wallet.detail.WalletDetailFragment
import com.inventiv.gastropaysdk.utils.TransactionType
import com.inventiv.gastropaysdk.utils.getResourceString
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WalletDetailFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockWebServer.start(8080)
        mockWebServer.dispatcher = (object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                with(request.path!!.replace("http://localhost:8080", "")) {
                    val response = MockResponse().setResponseCode(200)
                    return when (this) {
                        "/notify/invoice_send" -> {
                            response.setBody("")
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

        val transactionModel = TransactionModel(
            id = 1,
            name = "Bağcılar Starbucks",
            price = "₺26,60",
            date = 1631259969,
            transactionType = TransactionType.DEPOSIT,
            invoiceNumber = null
        )
        val bundle = Bundle().apply {
            putParcelable("PARAM_TRANSACTION", transactionModel)
        }

        val scenario =
            launchFragmentInContainer<WalletDetailFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        val merchantNameTitle =
            getResourceString(R.string.transaction_detail_merchant_label_gastropay_sdk)

        val amountTitle =
            getResourceString(R.string.transaction_detail_amount_label_gastropay_sdk)

        val dateTitle =
            getResourceString(R.string.transaction_detail_date_label_gastropay_sdk)

        //region Merchant Name
        Espresso.onView(ViewMatchers.withId(R.id.merchantNameTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(merchantNameTitle)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.merchantNameValueTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(transactionModel.name)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //endregion

        //region Amount
        Espresso.onView(ViewMatchers.withId(R.id.amountTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(amountTitle)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.amountValueTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(transactionModel.price)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //endregion

        //region Date
        Espresso.onView(ViewMatchers.withId(R.id.dateTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(dateTitle)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.dateValueTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("10.09.2021 - 10:46")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //endregion
    }

    @Test
    fun invoice_exist_layout_check() {

        val transactionModel = TransactionModel(
            id = 1,
            name = "Bağcılar Starbucks",
            price = "₺26,60",
            date = 1631259969,
            transactionType = TransactionType.DEPOSIT,
            invoiceNumber = "GA12345678901234"
        )
        val bundle = Bundle().apply {
            putParcelable("PARAM_TRANSACTION", transactionModel)
        }

        val scenario =
            launchFragmentInContainer<WalletDetailFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        val invoiceTitle =
            getResourceString(R.string.transaction_detail_bill_label_gastropay_sdk)

        Espresso.onView(ViewMatchers.withId(R.id.invoiceNumberTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(invoiceTitle)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.invoiceNumberValueTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(transactionModel.invoiceNumber)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun invoice_not_exist_layout_check() {

        val transactionModel = TransactionModel(
            id = 1,
            name = "Bağcılar Starbucks",
            price = "₺26,60",
            date = 1631259969,
            transactionType = TransactionType.DEPOSIT,
            invoiceNumber = null
        )
        val bundle = Bundle().apply {
            putParcelable("PARAM_TRANSACTION", transactionModel)
        }

        val scenario =
            launchFragmentInContainer<WalletDetailFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        Espresso.onView(ViewMatchers.withId(R.id.invoiceNumberLayout))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun transaction_type_deposit_check() {

        val transactionModel = TransactionModel(
            id = 1,
            name = "Bağcılar Starbucks",
            price = "₺26,60",
            date = 1631259969,
            transactionType = TransactionType.DEPOSIT,
            invoiceNumber = null
        )
        val bundle = Bundle().apply {
            putParcelable("PARAM_TRANSACTION", transactionModel)
        }

        val scenario =
            launchFragmentInContainer<WalletDetailFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        Espresso.onView(ViewMatchers.withId(R.id.transactionTypeValueTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.wallet_earn_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun transaction_type_withdraw_check() {

        val transactionModel = TransactionModel(
            id = 1,
            name = "Bağcılar Starbucks",
            price = "₺26,60",
            date = 1631259969,
            transactionType = TransactionType.WITHDRAW,
            invoiceNumber = null
        )
        val bundle = Bundle().apply {
            putParcelable("PARAM_TRANSACTION", transactionModel)
        }

        val scenario =
            launchFragmentInContainer<WalletDetailFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        Espresso.onView(ViewMatchers.withId(R.id.transactionTypeValueTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.wallet_spend_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun transaction_type_cancel_withdraw_check() {

        val transactionModel = TransactionModel(
            id = 1,
            name = "Bağcılar Starbucks",
            price = "₺26,60",
            date = 1631259969,
            transactionType = TransactionType.CANCEL_WITHDRAW,
            invoiceNumber = null
        )
        val bundle = Bundle().apply {
            putParcelable("PARAM_TRANSACTION", transactionModel)
        }

        val scenario =
            launchFragmentInContainer<WalletDetailFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        Espresso.onView(ViewMatchers.withId(R.id.transactionTypeValueTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.wallet_cancel_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun transaction_type_cancel_deposit_check() {

        val transactionModel = TransactionModel(
            id = 1,
            name = "Bağcılar Starbucks",
            price = "₺26,60",
            date = 1631259969,
            transactionType = TransactionType.CANCEL_DEPOSIT,
            invoiceNumber = null
        )
        val bundle = Bundle().apply {
            putParcelable("PARAM_TRANSACTION", transactionModel)
        }

        val scenario =
            launchFragmentInContainer<WalletDetailFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        Espresso.onView(ViewMatchers.withId(R.id.transactionTypeValueTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.wallet_cancel_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun invoice_send_200_check() {

        val transactionModel = TransactionModel(
            id = 1,
            name = "Bağcılar Starbucks",
            price = "₺26,60",
            date = 1631259969,
            transactionType = TransactionType.CANCEL_DEPOSIT,
            invoiceNumber = "12345678"
        )
        val bundle = Bundle().apply {
            putParcelable("PARAM_TRANSACTION", transactionModel)
        }

        val scenario =
            launchFragmentInContainer<WalletDetailFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        Espresso.onView(ViewMatchers.withId(R.id.invoiceNumberLayout)).perform(ViewActions.click())
    }
}