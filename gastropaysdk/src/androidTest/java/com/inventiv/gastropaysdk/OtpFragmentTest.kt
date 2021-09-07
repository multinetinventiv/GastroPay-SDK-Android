package com.inventiv.gastropaysdk

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inventiv.gastropaysdk.ui.otp.OtpFragment
import com.inventiv.gastropaysdk.utils.enqueueResponse
import com.inventiv.gastropaysdk.utils.formatPhoneNumber
import com.inventiv.gastropaysdk.utils.getResourceString
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class OtpFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockWebServer.start(8080)
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun view_titles_check() {
        val phoneNumber = "5071234450"
        val tenSec = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + 10
        val bundle = Bundle().apply {
            putString("PARAM_PHONE_NUMBER", phoneNumber)
            putString("PARAM_VERIFICATION_CODE", "1234")
            putString("PARAM_END_TIME", tenSec.toString())
        }
        val scenario =
            launchFragmentInContainer<OtpFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        val infoText = String.format(
            getResourceString(R.string.otp_sms_label_gastropay_sdk),
            phoneNumber.formatPhoneNumber()
        )

        onView(ViewMatchers.withId(R.id.infoTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(infoText)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun wait_time_end() {
        val waitSec = 3L
        val phoneNumber = "5071234450"
        val tenSec = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + waitSec
        val bundle = Bundle().apply {
            putString("PARAM_PHONE_NUMBER", phoneNumber)
            putString("PARAM_VERIFICATION_CODE", "1234")
            putString("PARAM_END_TIME", tenSec.toString())
        }
        val scenario =
            launchFragmentInContainer<OtpFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )

        scenario.onFragment { fragment ->

        }

        onView(ViewMatchers.withId(R.id.resendButton))
            .check(ViewAssertions.matches(not(ViewMatchers.isEnabled())))

        Thread.sleep(waitSec * 1_000)

        val endText =
            String.format(getResourceString(R.string.otp_timer_label_gastropay_sdk), "00:00")

        onView(ViewMatchers.withId(R.id.timerInfoTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(endText)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.resendButton))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

    @Test
    fun test_server_success() {
        mockWebServer.enqueueResponse("otp-confirm-200.json", 200)

        val phoneNumber = "5071234450"
        val tenSec = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + 10

        val bundle = Bundle().apply {
            putString("PARAM_PHONE_NUMBER", phoneNumber)
            putString("PARAM_VERIFICATION_CODE", "1234")
            putString("PARAM_END_TIME", tenSec.toString())
        }
        val scenario =
            launchFragmentInContainer<OtpFragment>(
                fragmentArgs = bundle,
                themeResId = R.style.Theme_GastroPaySdk
            )

        scenario.onFragment { fragment ->

        }

        onView(ViewMatchers.withId(R.id.loadingLayout))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        onView(ViewMatchers.withId(R.id.pinEntryEditText))
            .perform(clearText(), typeText("111111"))
    }

}