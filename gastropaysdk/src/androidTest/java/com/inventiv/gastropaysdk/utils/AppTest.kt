package com.inventiv.gastropaysdk.utils

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.shared.GastroPaySdkListener
import com.inventiv.gastropaysdk.shared.Language

class AppTest : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        GastroPaySdk.init(
            application = this,
            language = Language.TR,
            listener = object : GastroPaySdkListener {
                override fun onInitialized(isInitialized: Boolean) {
                    super.onInitialized(isInitialized)
                    Log.d("isInitialized", isInitialized.toString())
                }
            })
    }
}