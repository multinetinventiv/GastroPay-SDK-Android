package com.inventiv.gastropaysdk.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.databinding.LayoutToolbarGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.REFERENCE_UNDEFINED

internal class GastroPaySdkToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutToolbarGastropaySdkBinding =
        LayoutToolbarGastropaySdkBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )

    init {
        addView(binding.root)
    }

    fun onLeftIconClick(operation: () -> Unit) {
        binding.layoutLeftGastroPaySdk.setOnClickListener {
            operation()
        }
    }

    fun onRightIconClick(operation: () -> Unit) {
        binding.layoutRightGastroPaySdk.setOnClickListener {
            operation()
        }
    }

    fun setCenterImage(resourceId: Int) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.imageLogoGastroPaySdk.visibility = GONE
            return
        }
        binding.imageLogoGastroPaySdk.visibility = VISIBLE
        binding.imageLogoGastroPaySdk.setImageResource(resourceId)
        binding.textTitleGastroPaySdk.visibility = GONE
    }

    fun setLeftIcon(resourceId: Int, @ColorRes color: Int? = null) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.layoutLeftGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutLeftGastroPaySdk.visibility = VISIBLE
        binding.imageToolbarLeftGastroPaySdk.setImageResource(resourceId)
        if (color != null) {
            binding.imageToolbarLeftGastroPaySdk.setColorFilter(
                ContextCompat.getColor(context, color),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    fun setRightIcon(resourceId: Int, @ColorRes color: Int? = null) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.layoutRightGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutRightGastroPaySdk.visibility = VISIBLE
        binding.imageToolbarRightGastroPaySdk.setImageResource(resourceId)
        if (color != null) {
            binding.imageToolbarRightGastroPaySdk.setColorFilter(
                ContextCompat.getColor(context, color),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    fun _setBackgroundColor(@ColorRes color: Int) {
        binding.rootLayoutToolbarGastroPaySdk.setBackgroundColor(
            ContextCompat.getColor(
                context,
                color
            )
        )
    }

    private fun setTitle(title: String) {
        if (title.isEmpty()) {
            binding.textTitleGastroPaySdk.visibility = GONE
            return
        }
        binding.imageLogoGastroPaySdk.visibility = GONE
        binding.textTitleGastroPaySdk.visibility = VISIBLE
        binding.textTitleGastroPaySdk.text = title
    }

    fun setTitle(toolbarTitle: String?, @ColorRes color: Int? = null) {
        if (toolbarTitle.isNullOrEmpty()) {
            binding.textTitleGastroPaySdk.visibility = GONE
            return
        }
        binding.imageLogoGastroPaySdk.visibility = GONE
        binding.textTitleGastroPaySdk.visibility = VISIBLE
        binding.textTitleGastroPaySdk.text = toolbarTitle

        if (color != null) {
            binding.textTitleGastroPaySdk.setTextColor(ContextCompat.getColor(context, color))
        }
    }

    fun changeToMainStyle() {
        _setBackgroundColor(R.color.celtic_gastropay_sdk)
        setRightIcon(R.drawable.ic_close_gastropay_sdk, android.R.color.white)
        setLeftIcon(R.drawable.ic_settings_gastropay_sdk, android.R.color.white)
    }

    fun changeToLoginStyle() {
        _setBackgroundColor(android.R.color.transparent)
        setLeftIcon(R.drawable.ic_arrow_back_gastropay_sdk, android.R.color.black)
        setRightIcon(REFERENCE_UNDEFINED)
    }

}