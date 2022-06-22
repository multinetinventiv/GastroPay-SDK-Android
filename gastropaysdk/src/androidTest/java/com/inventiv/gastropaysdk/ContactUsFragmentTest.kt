package com.inventiv.gastropaysdk

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inventiv.gastropaysdk.ui.contactus.ContactUsFragment
import com.inventiv.gastropaysdk.utils.SP_INIT_NAME
import com.inventiv.gastropaysdk.utils.SP_PARAM_SETTINGS
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.SPUtils
import com.inventiv.gastropaysdk.utils.getResourceString
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ContactUsFragmentTest {

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockkStatic(SPUtils::class)
        val builder = StringBuilder().apply {
            append("{").append("\n")
            append("\"faq\": \"https://www.google.com\"").append(",").append("\n")
            append("\"phoneNumber\": \"08001234567\"").append(",").append("\n")
            append("\"email\": \"test@test.com\"").append("\n")
                .append("}")
        }
        every { SPUtils.getInstance(SP_INIT_NAME).getString(SP_PARAM_SETTINGS, "") }.returns(
            builder.toString()
        )
    }

    @After
    @Throws(IOException::class)
    fun teardown() {

    }

    @Test
    fun view_titles_check() {
        val scenario =
            launchFragmentInContainer<ContactUsFragment>(
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        val title = getResourceString(R.string.contact_us_title_gastropay_sdk)
        val writeButtonTitle = getResourceString(R.string.contact_us_write_gastropay_sdk)

        Espresso.onView(ViewMatchers.withId(R.id.titleToolbar))
            .check(ViewAssertions.matches(ViewMatchers.withText(title)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.writeButton))
            .check(ViewAssertions.matches(ViewMatchers.withText(writeButtonTitle)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun write_button_click_check() {
        val scenario =
            launchFragmentInContainer<ContactUsFragment>(
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        Espresso.onView(ViewMatchers.withId(R.id.writeButton))
            .check(ViewAssertions.matches(ViewMatchers.isClickable()))

        Espresso.onView(ViewMatchers.withId(R.id.writeButton)).perform(ViewActions.click())
    }
}