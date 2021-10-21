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
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.data.ApiError
import com.inventiv.gastropaysdk.data.response.ErrorResponse
import com.inventiv.gastropaysdk.data.response.SettingsResponse
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.GsonUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.SPUtils
import com.tapadoo.alerter.Alerter
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit

// region Context Extensions
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
internal fun Context.openPhoneDialer(phoneNumber: String): Boolean = try {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    true
} catch (e: ActivityNotFoundException) {
    Log.e("openPhoneDialer", e.toString())
    false
}

internal fun Context.openGoogleMap(latitude: Double, longitude: Double) {
    val uri = Uri.parse("http://maps.google.com/maps?daddr=${latitude},${longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
}

internal fun Context.isValidGlideContext() =
    this !is Activity || (!this.isDestroyed && !this.isFinishing)
// endregion ImageView Extensions

// region TextView Extensions
internal fun TextView.markdownText(
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
// endregion TextView Extensions

// region Api Extensions
internal fun ApiError.handleError(activity: Activity) {
    var title = this.code.toString()
    var message = this.message

    val errorResponse = this.body.bodyToErrorResponse(title, message)

    title = errorResponse.first
    message = errorResponse.second

    Alerter.create(activity)
        .setTitle(title)
        .setText(message)
        .setIcon(R.drawable.ic_warning_gastropay_sdk)
        .setBackgroundColorRes(R.color.reddish_orange_gastropay_sdk)
        .show()
}

internal fun ResponseBody?.bodyToErrorResponse(
    defaultTitle: String = "Error",
    defaultMessage: String = "Error occurred."
): Pair<String, String> {
    var pair = Pair(defaultTitle, defaultMessage)
    try {
        val errorResponse = GsonUtils.fromJson(this!!.charStream(), ErrorResponse::class.java)
        pair = Pair(errorResponse.resultCode, errorResponse.resultMessage)
    } catch (e: Exception) {
        LogUtils.e(e)
    }
    return pair
}
// endregion Api Extensions

// region Fragment Extensions
internal fun Fragment.showError(message: String?) {
    Alerter.create(requireActivity())
        .setTitle(message ?: String())
        .setIcon(R.drawable.ic_warning_gastropay_sdk)
        .setBackgroundColorRes(R.color.reddish_orange_gastropay_sdk)
        .show()
}
// endregion ImageView Extensions

// region ImageView Extensions
internal fun AppCompatImageView.loadImage(imageUrl: String?) {
    if (this.context.isValidGlideContext()) {
        Glide.with(this.context).load(imageUrl).into(this)
    }
}

internal fun AppCompatImageView.loadImage(fragment: Fragment, imageUrl: String?) {
    Glide.with(fragment).load(imageUrl).into(this)
}

internal fun AppCompatImageView.loadImage(activity: Activity, imageUrl: String?) {
    Glide.with(activity).load(imageUrl).into(this)
}
// endregion ImageView Extensions

/**
 * @return seconds
 */
internal fun Long.calculateDifferenceWithCurrentTime(): Long {
    val currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    return (this - currentTime)
}

internal fun SettingsResponse.saveSettings() {
    SPUtils.getInstance(SP_INIT_NAME).put(SP_PARAM_SETTINGS, GsonUtils.toJson(this))
}

internal fun getSettings(): SettingsResponse {
    return GsonUtils.fromJson(
        SPUtils.getInstance(SP_INIT_NAME).getString(SP_PARAM_SETTINGS, ""),
        SettingsResponse::class.java
    )
}
