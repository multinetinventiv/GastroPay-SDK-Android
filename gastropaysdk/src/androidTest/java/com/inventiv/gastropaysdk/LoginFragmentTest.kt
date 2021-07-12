package com.inventiv.gastropaysdk

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.ui.MainActivity
import com.inventiv.gastropaysdk.ui.login.LoginFragment
import com.inventiv.gastropaysdk.utils.enqueueResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

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
    fun if_user_not_logged_in_first_fragment_is_login() {
        val scenario = launchActivity<MainActivity>()
        scenario.onActivity { activity ->
            val loginFragment = activity.supportFragmentManager.fragments[0]
            Truth.assertThat(loginFragment).isInstanceOf(LoginFragment::class.java)
        }
    }

    @Test
    fun view_titles_check() {
        val scenario =
            launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_GastroPaySdk)
        scenario.onFragment { fragment ->

        }

        onView(withId(R.id.phoneTextInputEditText))
            .check(matches(withHint(R.string.login_view_phone_number_hint_gastropay_sdk)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun enter_phone_check_button() {
        val scenario =
            launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_GastroPaySdk)

        onView(withId(R.id.loginButton))
            .check(matches(not(isEnabled())))

        onView(withId(R.id.phoneTextInputEditText))
            .perform(clearText(), typeText("05071234567"))

        onView(withId(R.id.loginButton))
            .check(matches(isEnabled()))
    }

    @Test
    fun enter_phone_mock_server_success() {

        mockWebServer.enqueueResponse("login-200.json", 200)

        val scenario =
            launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_GastroPaySdk)
        scenario.onFragment { fragment ->

        }

        onView(withId(R.id.loadingLayout))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.loginButton))
            .check(matches(not(isEnabled())))

        onView(withId(R.id.phoneTextInputEditText))
            .perform(clearText(), typeText("05071234567"))

        onView(withId(R.id.loginButton))
            .perform(click())
    }

    @Test
    fun enter_phone_mock_server_return_error() {

        mockWebServer.enqueueResponse("login-error-400.json", 400)

        val scenario =
            launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_GastroPaySdk)

        onView(withId(R.id.loadingLayout))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.loginButton))
            .check(matches(not(isEnabled())))

        onView(withId(R.id.phoneTextInputEditText))
            .perform(clearText(), typeText("05071234567"))

        onView(withId(R.id.loginButton))
            .perform(click())

    }

    @Test
    fun enter_phone_mock_server_return_corrupt_json() {

        mockWebServer.enqueueResponse("general-corrupt-json.json", 200)

        val scenario =
            launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_GastroPaySdk)

        onView(withId(R.id.loadingLayout))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.loginButton))
            .check(matches(not(isEnabled())))

        onView(withId(R.id.phoneTextInputEditText))
            .perform(clearText(), typeText("05071234567"))

        onView(withId(R.id.loginButton))
            .perform(click())
    }

}