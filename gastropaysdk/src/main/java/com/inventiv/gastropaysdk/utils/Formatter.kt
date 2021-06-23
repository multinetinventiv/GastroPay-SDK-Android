package com.inventiv.gastropaysdk.utils

const val GSM_NUMBER_REGEX = "(\\d{3})(\\d{3})(\\d{2})(\\d{2})"
const val GSM_NUMBER_REPLACEMENT = "0($1) $2 $3 $4"

fun String.formatPhoneNumber() =
    this.replaceFirst(GSM_NUMBER_REGEX.toRegex(), GSM_NUMBER_REPLACEMENT)