package com.inventiv.gastropaysdk.shared

import android.app.Application
import android.content.Intent
import com.inventiv.gastropaysdk.BuildConfig
import com.inventiv.gastropaysdk.GastroPaySdkComponent
import com.inventiv.gastropaysdk.activity.MainActivity
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.Utils

object GastroPaySdk {

    private lateinit var gastroPaySdkComponent: GastroPaySdkComponent

    internal fun getComponent() = gastroPaySdkComponent


    @JvmStatic
    fun setLanguage(language: Language) = getComponent().setLanguage(language)

    @JvmOverloads
    @JvmStatic
    fun init(
        application: Application,
        environment: Environment = Environment.PRODUCTION,
        language: Language? = null,
        listener: GastroPaySdkListener,
    ) {
        Utils.init(application)
        LogUtils.getConfig().isLogSwitch = BuildConfig.DEBUG

        Intent(application, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            application.startActivity(this)
        }

        this.gastroPaySdkComponent = GastroPaySdkComponent(
            appContext = application,
            environment = environment,
            language = language
        )

        listener.onInitialized(true)
    }

}