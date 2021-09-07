package com.inventiv.gastropaysdk

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inventiv.gastropaysdk.data.response.AmountModel
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.ui.pay.validate.PayValidateFragment
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
class PayValidateFragmentTest {

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
                        "/pos/provision_information" -> {  //1
                            response.setBody(readTestResourceFile("pos-provision_information-200.json"))
                        }
                        "/wallet/cards" -> { //2
                            response.setBody(readTestResourceFile("wallet-cards-200.json"))
                        }
                        else -> response
                    }
                }
            }
        })

        val bundle = Bundle().apply {
            putParcelable(
                "PARAM_PROVISION_DATA", ProvisionInformationResponse(
                    merchantId = "1234",
                    token = "3456",
                    amount = AmountModel(2.0, "", "", "₺2"),
                    merchantName = "Bağcılar Sturbucks",
                    imageUrl = "https://google.com/test.png",
                    totalAmount = AmountModel(2.0, "", "", "₺2"),
                    availableAmount = AmountModel(3.0, "", "", "₺3"),
                    usingAvailableAmount = AmountModel(4.0, "", "", "₺4"),
                    merchantUid = "6789",
                    callType = null,
                    terminalUid = "0"
                )
            )
        }

        val scenario =
            launchFragmentInContainer<PayValidateFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun view_titles_check() {

        val title =
            getResourceString(R.string.payment_confirmation_amount_payable_title_gastropay_sdk)
        val selectCardTitle =
            getResourceString(R.string.payment_confirmation_select_bank_card_label_gastropay_sdk)

        Espresso.onView(ViewMatchers.withId(R.id.amountTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(title)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.amountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("₺2")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.valueOrderTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("₺2")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.selectCardTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(selectCardTitle)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun view_points_click_check() {
        //Before Click
        Espresso.onView(ViewMatchers.withId(R.id.useButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.payment_confirmation_spendable_amount_button_text_gastropay_sdk))))

        Espresso.onView(ViewMatchers.withId(R.id.valueTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.payment_confirmation_spendable_amount_label_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //Click
        Espresso.onView(ViewMatchers.withId(R.id.useButton)).perform(ViewActions.click())

        //After Click
        Espresso.onView(ViewMatchers.withId(R.id.useButton))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.payment_confirmation_spendable_amount_button_cancel_text_gastropay_sdk))))

        Espresso.onView(ViewMatchers.withId(R.id.valueTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.payment_confirmation_spended_amount_label_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}