package com.inventiv.gastropaysdk.utils

import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.widget.EditText
import java.util.*

interface MaskWatcherView {

    fun beforeTextTriggered() {}

    fun onTextTriggered() {}

    fun afterTextTriggered() {}
}

class PhoneNumberTextWatcher(editText: EditText, maskWatcherView: MaskWatcherView) :
    TextWatcher {
    private val mask = "0(5##) ### ## ##"
    private val editText: EditText
    private val defaultFilter: Array<InputFilter>
    private val maskFilter: Array<InputFilter>
    private var isRunning = false
    private var isDeleting = false
    private var isCopyPaste = false
    private val maskWatcherView: MaskWatcherView
    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        isDeleting = count > after
        maskWatcherView.beforeTextTriggered()
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        isCopyPaste = count > 2
        maskWatcherView.onTextTriggered()
    }

    override fun afterTextChanged(editable: Editable) {
        if (isRunning || isDeleting) {
            maskWatcherView.afterTextTriggered()
            return
        }
        isRunning = true
        if (isCopyPaste) {
            val clearedPhoneNumber = clearPhoneNumber(editable.toString())
            editable.replace(0, editable.length, clearedPhoneNumber.formatPhoneNumber())
            isRunning = false
            maskWatcherView.afterTextTriggered()
            return
        }
        if (!isNumeric(editable.toString().substring(editable.length - 1))) {
            editText.filters = defaultFilter
            editable.replace(editable.length - 1, editable.length, "")
        } else {
            editText.filters = maskFilter
            val editableLength = editable.length // => 0(5
            if (editableLength < mask.length && editableLength != 0) {
                if (mask[editableLength - 1] != MASK_CHAR) {
                    val unmaskedChars = getSequentialUnmaskedChars(editableLength - 1) // 0(5
                    val lastUnMaskedChars = unmaskedChars[unmaskedChars.length - 1] // 5
                    if (lastUnMaskedChars == editable[editableLength - 1]) {
                        val length = unmaskedChars.length
                        val unmaskedCharWithoutLast = unmaskedChars.substring(0, length - 1) // 0(
                        editable.insert(editableLength - 1, unmaskedCharWithoutLast)
                    } else if (editable.toString() == "0") {
                        val length = unmaskedChars.length // 0(
                        val unmaskedCharWithoutLast = unmaskedChars.substring(0, length - 1)
                        editable.replace(
                            editableLength - 1,
                            editableLength,
                            unmaskedCharWithoutLast
                        )
                    } else {
                        editable.insert(editableLength - 1, unmaskedChars)
                    }
                } else if (mask[editableLength] != MASK_CHAR) {
                    editable.append(mask[editableLength])
                }
            }
        }
        isRunning = false
        maskWatcherView.afterTextTriggered()
    }

    private fun clearPhoneNumber(_phoneNumber: String): String {
        var phoneNumber = _phoneNumber
        phoneNumber = phoneNumber
            .replace(" ".toRegex(), "")
            .replace("[()]".toRegex(), "")
        if (phoneNumber.startsWith("90")) {
            phoneNumber = phoneNumber.replaceFirst("90".toRegex(), "")
        }
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.replaceFirst("0".toRegex(), "")
        }
        return phoneNumber
    }

    private fun getSequentialUnmaskedChars(position: Int): String {
        var str: String = ""
        for (i in position until mask.length) {
            val c = mask[i]
            str += if (c != MASK_CHAR) {
                c
            } else {
                break
            }
        }
        return str
    }

    private fun isNumeric(str: String): Boolean {
        return str.matches("^(0|[1-9][0-9]*)$".toRegex())
    }

    private val specialCharPositions: ArrayList<Int?>
        private get() {
            val positions: ArrayList<Int?> = arrayListOf()
            for (i in mask.indices) {
                if (mask[i] != '#') {
                    positions.add(i)
                }
            }
            return positions
        }

    companion object {
        private const val MASK_CHAR = '#'
    }

    init {
        val res = editText.context.resources
        this.editText = editText
        this.maskWatcherView = maskWatcherView
        val defaultLength = 200
        defaultFilter = arrayOf(LengthFilter(defaultLength))
        maskFilter = arrayOf(LengthFilter(mask.length))
    }
}
