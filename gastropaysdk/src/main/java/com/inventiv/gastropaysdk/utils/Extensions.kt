package com.inventiv.gastropaysdk.utils

import android.content.Context
import com.inventiv.gastropaysdk.R

internal fun Context.getDistanceAsMeters(distance: Int): String {
    var distanceDoubleValue = distance.toDouble()
    return if (distanceDoubleValue > METER_THRESHOLD) {
        distanceDoubleValue /= METER_TO_KM_CONVERTER
        resources.getString(
            R.string.merchant_distance_label_km_gastropay_sdk,
            String.format("%.2f", distanceDoubleValue)
        )
    } else {
        resources.getString(R.string.merchant_distance_label_mt_gastropay_sdk, distance.toString())
    }
}