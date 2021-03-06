package com.inventiv.gastropaysdk.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val GSM_NUMBER_REGEX = "(\\d{3})(\\d{3})(\\d{2})(\\d{2})"
const val GSM_NUMBER_REPLACEMENT = "0($1) $2 $3 $4"
const val MARKDOWN_LINK_REGEX = "\\[([^\\[\\]]*)\\]\\((.*?)\\)"

fun String.formatPhoneNumber() =
    this.replaceFirst(GSM_NUMBER_REGEX.toRegex(), GSM_NUMBER_REPLACEMENT)

fun String.toServicePhoneNumber() =
    this.replace("[^\\d]".toRegex(), "").substring(1)

@SuppressLint("SimpleDateFormat")
fun Long.formatDate(format: String): String {
    val timeMillis = TimeUnit.SECONDS.toMillis(this)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeMillis
    val simpleDateFormat = SimpleDateFormat(format)
    return simpleDateFormat.format(calendar.time)
}