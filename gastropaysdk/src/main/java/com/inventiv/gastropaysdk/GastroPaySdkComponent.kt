package com.inventiv.gastropaysdk

import android.content.Context
import androidx.annotation.StringRes
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.api.NetworkModule
import com.inventiv.gastropaysdk.shared.Environment
import com.inventiv.gastropaysdk.shared.GastroPaySdkListener
import com.inventiv.gastropaysdk.shared.Language
import com.inventiv.gastropaysdk.utils.LIBRARY_GENERAL_LOG_TAG
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.getLanguage

internal class GastroPaySdkComponent(
    private val appContext: Context,
    private val environment: Environment,
    private var language: Language?,
    private val logging: Boolean?
) {
    val gastroPayService: GastroPayService
    var isUserLoggedIn: Boolean = true
    var globalGastroPaySdkListener: GastroPaySdkListener? = null

    init {
        val networkModule = NetworkModule(environment = environment, logging = logging())
        this.language = getLanguage(appContext, language)
        gastroPayService = networkModule.provideGastroPayService()
        prepareLogUtils()
    }

    private fun prepareLogUtils() {
        with(LogUtils.getConfig()) {
            isLogSwitch = logging()
            globalTag = LIBRARY_GENERAL_LOG_TAG
        }
    }

    fun logging() = logging ?: BuildConfig.DEBUG

    fun language() = requireNotNull(language)

    fun setLanguage(language: Language?) {
        this.language = getLanguage(appContext, language)
    }

    fun environment() = environment

    fun appContext() = appContext

    fun getString(@StringRes resId: Int, vararg args: Any): String {
        return appContext.getString(resId, args)
    }
}