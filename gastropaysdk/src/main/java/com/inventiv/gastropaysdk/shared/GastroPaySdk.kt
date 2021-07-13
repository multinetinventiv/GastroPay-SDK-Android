package com.inventiv.gastropaysdk.shared

import android.app.Application
import android.content.Context
import android.content.Intent
import com.inventiv.gastropaysdk.BuildConfig
import com.inventiv.gastropaysdk.GastroPaySdkComponent
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.data.GastroPayUser
import com.inventiv.gastropaysdk.ui.MainActivity
import com.inventiv.gastropaysdk.utils.AESHelper
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.Utils
import org.jetbrains.annotations.TestOnly

object GastroPaySdk {

    private lateinit var gastroPaySdkComponent: GastroPaySdkComponent

    internal fun getComponent() = gastroPaySdkComponent

    @JvmStatic
    fun setLanguage(language: Language) = getComponent().setLanguage(language)

    @JvmStatic
    fun setGlobalGastroPaySdkListener(gastroPaySdkListener: GastroPaySdkListener) {
        getComponent().globalGastroPaySdkListener = gastroPaySdkListener
    }

    @JvmOverloads
    @JvmStatic
    @Throws(GastroPaySdkException::class)
    fun init(
        application: Application,
        environment: Environment = Environment.PRODUCTION,
        obfuscationKey: String,
        language: Language? = null,
        logging: Boolean? = null,
        listener: GastroPaySdkListener? = null,
    ) {
        Utils.init(application)

        try {
            environment.baseUrl = AESHelper.decrypt(environment.encryptedBaseUrl, obfuscationKey)
        } catch (exception: Exception) {
            throw GastroPaySdkException.SecurityException(application.getString(R.string.security_exception_message))
        }

        this.gastroPaySdkComponent = GastroPaySdkComponent(
            appContext = application,
            environment = environment,
            language = language,
            logging = logging ?: BuildConfig.DEBUG
        ).apply {
            globalGastroPaySdkListener = listener
        }

        getComponent().globalGastroPaySdkListener?.onInitialized(true)
    }

    @JvmStatic
    fun start(context: Context, authToken: String? = null) {
        Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
            GastroPayUser.authToken = authToken
        }
    }

    @TestOnly
    internal fun init(
        application: Application,
        language: Language? = null,
        listener: GastroPaySdkListener? = null,
    ) {
        Utils.init(application)

        val environment = Environment.TEST
        environment.baseUrl = "http://localhost:8080"

        this.gastroPaySdkComponent = GastroPaySdkComponent(
            appContext = application,
            environment = environment,
            language = language,
            logging = true
        ).apply {
            globalGastroPaySdkListener = listener
        }

        getComponent().globalGastroPaySdkListener?.onInitialized(true)
    }

}