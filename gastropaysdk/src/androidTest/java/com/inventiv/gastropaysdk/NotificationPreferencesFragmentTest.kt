package com.inventiv.gastropaysdk

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesChannelType
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesFragment
import com.inventiv.gastropaysdk.utils.ChildViewAction
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
class NotificationPreferencesFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockWebServer.start(8080)
        mockWebServer.dispatcher = (object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                with(request.path!!.replace("http://localhost:8080", "")) {
                    val response = MockResponse()
                    if (startsWith("/auth/update_notification_preferences/")) {
                        return response.setResponseCode(204)
                    }
                    return when (this) {
                        "/auth/notification_preferences" -> {
                            response
                                .setBody(readTestResourceFile("notification_preferences-200.json"))
                                .setResponseCode(200)
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
            launchFragmentInContainer<NotificationPreferencesFragment>(
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        val smsTitle = NotificationPreferencesChannelType.SMS.name
        val emailTitle = NotificationPreferencesChannelType.MAIL.name

        Espresso.onView(ViewMatchers.withId(R.id.notificationPreferencesRecyclerView))
            .check(
                ViewAssertions.matches(
                    hasDescendant(withText(smsTitle))
                )
            )
            .check(
                ViewAssertions.matches(
                    hasDescendant(withText(emailTitle))
                )
            )
    }

    @Test
    fun preference_update_test() {
        val scenario =
            launchFragmentInContainer<NotificationPreferencesFragment>(
                themeResId = R.style.Theme_GastroPaySdk
            )
        scenario.onFragment { fragment ->

        }

        Espresso.onView(ViewMatchers.withId(R.id.notificationPreferencesRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    ChildViewAction(R.id.switchNotification)
                )
            )
    }
}