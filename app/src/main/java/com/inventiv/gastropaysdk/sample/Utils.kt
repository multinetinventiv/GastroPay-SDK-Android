package com.inventiv.gastropaysdk.sample

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity


const val PREF_INFOS = "PREF_INFOS"

fun FragmentActivity.getSharedPref(): SharedPreferences {
    return this.getSharedPreferences("GastroPaySdkSample", Context.MODE_PRIVATE)
}

fun getVersionText(): String {
    return "v${BuildConfig.VERSION_NAME}"
}