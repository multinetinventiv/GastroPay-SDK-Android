package com.inventiv.gastropaysdk

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.shared.Environment
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GastroPaySdkTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.inventiv.gastropaysdk.test", appContext.packageName)
    }

    @Test
    fun sdkInitialized() {
        Truth.assertThat(GastroPaySdk.getComponent()).isNotNull()
    }

    @Test
    fun environmentIsTest() {
        Truth.assertThat(GastroPaySdk.getComponent().environment()).isEqualTo(Environment.TEST)
    }
}