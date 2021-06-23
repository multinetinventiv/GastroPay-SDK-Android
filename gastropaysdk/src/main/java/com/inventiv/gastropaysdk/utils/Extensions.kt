package com.inventiv.gastropaysdk.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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

/**
 * This shows a UI with the number being dialed, allowing the user to explicitly initiate the call.
 * @return false if exception exist
 */
fun Context.openPhoneDialer(phoneNumber: String): Boolean = try {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    true
} catch (e: ActivityNotFoundException) {
    Log.e("openPhoneDialer", e.toString())
    false
}

fun Context.openGoogleMap(latitude: Double, longitude: Double) {
    val uri = Uri.parse("http://maps.google.com/maps?daddr=${latitude},${longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
}