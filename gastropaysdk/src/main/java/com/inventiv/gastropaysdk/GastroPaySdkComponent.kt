package com.inventiv.gastropaysdk

import android.content.Context
import androidx.annotation.StringRes
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.api.NetworkModule
import com.inventiv.gastropaysdk.shared.Environment
import com.inventiv.gastropaysdk.shared.GastroPaySdkListener
import com.inventiv.gastropaysdk.shared.Language
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.getLanguage

internal class GastroPaySdkComponent(
    private val appContext: Context,
    private val environment: Environment,
    private var language: Language?
) {
    val gastroPayService: GastroPayService
    var globalGastroPaySdkListener: GastroPaySdkListener? = null

    init {
        val networkModule = NetworkModule(environment = environment)
        this.language = getLanguage(appContext, language)
        gastroPayService = networkModule.provideGastroPayService()
        LogUtils.d(BuildConfig.DEBUG)
    }

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