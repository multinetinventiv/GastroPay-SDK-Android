package com.inventiv.gastropaysdk.utils

import android.app.Application
import android.util.Log
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.shared.GastroPaySdkListener
import com.inventiv.gastropaysdk.shared.Language

class AppTest : Application() {
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