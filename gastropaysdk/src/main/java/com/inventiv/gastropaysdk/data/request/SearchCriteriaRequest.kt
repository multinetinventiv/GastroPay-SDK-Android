package com.inventiv.gastropaysdk.data.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class SearchCriteria(
    val searchName: String? = null,
    val tags: String? = null,
    val cityId: String? = null
) : Parcelable