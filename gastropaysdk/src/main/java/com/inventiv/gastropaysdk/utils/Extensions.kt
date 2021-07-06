package com.inventiv.gastropaysdk.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.data.response.ErrorResponse
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.GsonUtils
import com.tapadoo.alerter.Alerter
import okhttp3.ResponseBody

internal fun Context.getDistanceAsMeters(distance: Int): String {
    var distanceDoubleValue = distance.toDouble()
    return if (distanceDoubleValue > METER_THRESHOLD) {
        distanceDoubleValue /= METER_TO_KM_CONVERTER
        resources.getString(
            R.string.merchants_merchant_distance_label_km_gastropay_sdk,
            String.format("%.2f", distanceDoubleValue)
        )
    } else {
        resources.getString(
            R.string.merchants_merchant_distance_label_mt_gastropay_sdk,
            distance.toString()
        )
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

fun TextView.markdownText(
    content: String
) {
    val spannable = SpannableStringBuilder()
    var startIndex = 0
    val matches = MARKDOWN_LINK_REGEX.toRegex().findAll(content)
    for (match in matches) {
        spannable.append(content.substring(startIndex, match.range.first))
        val firstIndexBeforeLink = spannable.length
        spannable.append(match.groupValues[1])
        val link = match.groupValues[2]
        val lastIndex = firstIndexBeforeLink + match.groupValues[1].length
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    context.startActivity(browserIntent)
                }
            },
            firstIndexBeforeLink,
            lastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex = match.range.last + 1
    }
    spannable.append(content.substring(startIndex))
    text = spannable
    movementMethod = LinkMovementMethod.getInstance()
}

fun Context.isValidGlideContext() = this !is Activity || (!this.isDestroyed && !this.isFinishing)

fun ResponseBody.handleError(activity: Activity) {
    val errorResponse = GsonUtils.fromJson(this.charStream(), ErrorResponse::class.java)
    Alerter.create(activity)
        .setTitle("${errorResponse.resultCode}\n${errorResponse.resultMessage}")
        .setIcon(R.drawable.ic_warning_gastropay_sdk)
        .setBackgroundColorRes(R.color.reddish_orange_gastropay_sdk)
        .show()
}
