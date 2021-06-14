package com.inventiv.gastropaysdk.sample

import android.os.Parcelable
import com.inventiv.gastropaysdk.shared.Environment
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoModel(
    val environment: Environment,
    val obfuscationKey: String
) : Parcelable