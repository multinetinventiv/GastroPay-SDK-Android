package com.inventiv.gastropaysdk.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.inventiv.gastropaysdk.shared.Language
import java.util.*

internal val DEFAULT_LANGUAGE = Language.TR

internal fun updateBaseContextLocale(context: Context, language: String): Context? {
    val locale = Locale(language)
    Locale.setDefault(locale)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        updateResourcesLocale(context, locale)
    } else updateResourcesLocaleLegacy(context, locale)
}

@TargetApi(Build.VERSION_CODES.N)
internal fun updateResourcesLocale(
    context: Context,
    locale: Locale
): Context? {
    val configuration = context.resources.configuration
    configuration.setLocale(locale)
    return context.createConfigurationContext(configuration)
}

internal fun updateResourcesLocaleLegacy(
    context: Context,
    locale: Locale
): Context {
    val resources = context.resources
    val configuration: Configuration = resources.configuration
    configuration.locale = locale
    resources.updateConfiguration(configuration, resources.displayMetrics)
    return context
}

internal fun getLanguage(ctx: Context, language: Language?): Language {
    return if (language == null) {
        deviceLanguage(ctx)
    } else {
        appLanguage(language)
    }
}

internal fun appLanguage(appLanguage: Language): Language {
    val languages = Language.values()
    for (language in languages) {
        if (language.id == appLanguage.id) {
            return language
        }
    }
    return DEFAULT_LANGUAGE
}

internal fun deviceLanguage(ctx: Context): Language {
    val config = ctx.resources.configuration
    val locale = if (Build.VERSION.SDK_INT >= 24) config.locales[0] else config.locale
    val languages = Language.values()
    for (language in languages) {
        if (language.id == locale.language) {
            return language
        }
    }
    return DEFAULT_LANGUAGE
}