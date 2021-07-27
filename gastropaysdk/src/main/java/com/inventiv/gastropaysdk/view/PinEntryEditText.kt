package com.inventiv.gastropaysdk.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.inventiv.gastropaysdk.R

internal class PinEntryEditText : AppCompatEditText {

    private var mSpace = 8f //24 dp by default, space between the lines
    private var mCharSize = 0f
    private var mNumChars = 4f
    private var mLineSpacing = 8f //8dp by default, height of the text from our lines
    private var mMaxLength = 4
    private val groupSpace = 1
    private val groupSize = 1
    private var mClickListener: OnClickListener? = null
    private var mLineStroke = 2f //1dp by default
    private var mLineStrokeSelected = 2f //2dp by default
    private var mLinesPaint: Paint? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    companion object {
        const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val multi = context.resources.displayMetrics.density
        mLineStroke *= multi
        mLineStrokeSelected *= multi
        mLinesPaint = Paint(paint)
        mLinesPaint!!.strokeWidth = mLineStroke
        setBackgroundResource(
            attrs.getAttributeResourceValue(
                XML_NAMESPACE_ANDROID,
                "background",
                0
            )
        )
        mSpace *= multi //convert to pixels for our density
        mLineSpacing *= multi //convert to pixels for our density
        mMaxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 4)
        mNumChars = mMaxLength.toFloat()

        super.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        })
        super.setOnClickListener { v ->
            setSelection(text!!.length)
            if (mClickListener != null) {
                mClickListener!!.onClick(v)
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mClickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }

    override fun onDraw(canvas: Canvas) {
        val availableWidth = width - paddingRight - paddingLeft
        mCharSize = if (mSpace < 0) {
            availableWidth / (mNumChars * 2 - 1)
        } else {
            (availableWidth - mSpace * (mNumChars - 1) - groupSpace * (mNumChars / groupSize - 1)) / mNumChars
        }
        var startX = paddingLeft
        val bottom = height - paddingBottom

        val text = text
        val textLength = text!!.length
        val textWidths = FloatArray(textLength)
        paint.getTextWidths(getText(), 0, textLength, textWidths)
        var i = 0
        while (i < mNumChars) {
            updateColorForLines(i == textLength)
            canvas.drawLine(
                startX.toFloat(), bottom.toFloat(), startX + mCharSize, bottom.toFloat(),
                mLinesPaint!!
            )
            if (getText()!!.length > i) {
                val middle = startX + mCharSize / 2
                canvas.drawText(
                    text, i, i + 1, middle - textWidths[0] / 2, bottom - mLineSpacing,
                    paint
                )
            }
            startX += if (mSpace < 0) {
                (mCharSize * 2).toInt()
            } else {
                val extraSpace = if (i % groupSize == 2) groupSpace else 0
                (mCharSize + mSpace + extraSpace).toInt()
            }
            i++
        }
    }

    private fun updateColorForLines(next: Boolean) {
        if (isFocused) {
            mLinesPaint!!.strokeWidth = mLineStrokeSelected
            mLinesPaint!!.color = Color.LTGRAY
            if (next) {
                mLinesPaint!!.color = ContextCompat.getColor(context, R.color.celtic_gastropay_sdk)
            }
        } else {
            mLinesPaint!!.strokeWidth = mLineStroke
            mLinesPaint!!.color = Color.LTGRAY
        }
    }
}
