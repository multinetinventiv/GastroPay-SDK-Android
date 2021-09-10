package com.inventiv.gastropaysdk

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inventiv.gastropaysdk.data.response.AmountModel
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.ui.pay.result.PayResultFragment
import com.inventiv.gastropaysdk.utils.getResourceString
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PayResultFragmentTest {

    @Test
    fun view_titles_check() {

        val bundle = Bundle().apply {
            putParcelable(
                "PARAM_PROVISION_DATA", ProvisionInformationResponse(
                    merchantId = "1234",
                    token = "3456",
                    amount = AmountModel(2.18, "", "", "₺2,18"),
                    merchantName = "Bağcılar Starbucks",
                    imageUrl = "https://google.com/test.png",
                    totalAmount = AmountModel(2.18, "", "", "₺2,18"),
                    availableAmount = AmountModel(3.0, "", "", "₺3"),
                    usingAvailableAmount = AmountModel(4.0, "", "", "₺4"),
                    merchantUid = "6789",
                    callType = null,
                    terminalUid = "0"
                )
            )
        }

        val scenario =
            launchFragmentInContainer<PayResultFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        val title =
            getResourceString(R.string.payment_success_payment_success_label_gastropay_sdk)

        Espresso.onView(ViewMatchers.withId(R.id.paymentSuccessTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(title)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.amountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("₺2,18")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.feedbackTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.payment_success_contact_us_header_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.feedbackDescTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.payment_success_contact_us_info_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.contactUsButton))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.payment_success_button_contact_us_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.endButton))
            .check(ViewAssertions.matches(ViewMatchers.withText(getResourceString(R.string.payment_success_done_button_text_gastropay_sdk))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .check(ViewAssertions.matches(ViewMatchers.isClickable()))

    }
}