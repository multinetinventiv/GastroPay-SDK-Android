package com.inventiv.gastropaysdk

import android.content.Context
import com.inventiv.gastropaysdk.shared.Environment
import com.inventiv.gastropaysdk.shared.Language
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.getLanguage

internal class GastroPaySdkComponent(
    private val appContext: Context,
    private val environment: Environment,
    private var language: Language?
) {
    init {
        this.language = getLanguage(appContext, language)
        LogUtils.d(BuildConfig.DEBUG)
    }

    fun language() = requireNotNull(language)

    fun setLanguage(language: Language?) {
        this.language = getLanguage(appContext, language)
    }
}