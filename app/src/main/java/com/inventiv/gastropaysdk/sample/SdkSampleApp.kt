package com.inventiv.gastropaysdk.sample

import androidx.multidex.MultiDexApplication

class SdkSampleApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        // NORMALLY GastroPaySdk.init method SHOULD BE CALLED HERE in Application onCreate method.

        // But it is moved to SdkStartActivity in this sample app for demonstration purposes
        // especially for dynamic environment choose

        /*GastroPaySdk.init(
                application = this,
                environment = Environment.TEST,
                language = Language.TR
        )*/
    }
}