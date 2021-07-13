package com.inventiv.gastropaysdk.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
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
        val a = context.obtainStyledAttributes(attrs, R.styleable.GastroPaySdkToolbar)
        try {
            val title = a.getString(R.styleable.GastroPaySdkToolbar_gastropay_sdk_toolbar_title)
            val leftIconId = a.getResourceId(
                R.styleable.GastroPaySdkToolbar_gastropay_sdk_toolbar_left_icon,
                REFERENCE_UNDEFINED
            )
            val rightIconId = a.getResourceId(
                R.styleable.GastroPaySdkToolbar_gastropay_sdk_toolbar_right_icon,
                REFERENCE_UNDEFINED
            )
            val centerImageId = a.getResourceId(
                R.styleable.GastroPaySdkToolbar_gastropay_sdk_toolbar_center_image,
                REFERENCE_UNDEFINED
            )
            val isTransparentBg = a.getBoolean(
                R.styleable.GastroPaySdkToolbar_gastropay_sdk_toolbar_transparent_bg,
                false
            )

            if (centerImageId != REFERENCE_UNDEFINED) {
                setCenterImage(centerImageId)
            } else if (!title.isNullOrEmpty()) {
                setTitle(title)
            } else {
                binding.imageLogoGastroPaySdk.visibility = GONE
                binding.textTitleGastroPaySdk.visibility = GONE
            }

            if (leftIconId != REFERENCE_UNDEFINED) {
                setLeftIcon(leftIconId)
            } else {
                binding.layoutLeftGastroPaySdk.visibility = GONE
            }

            if (rightIconId != REFERENCE_UNDEFINED) {
                setRightIcon(rightIconId)
            } else {
                binding.layoutRightGastroPaySdk.visibility = GONE
            }

            if (isTransparentBg) {
                setBackgroundTransparency(true)
            }

        } finally {
            a.recycle()
        }
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

    private fun setLeftIcon(resourceId: Int) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.layoutLeftGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutLeftGastroPaySdk.visibility = VISIBLE
        binding.imageToolbarLeftGastroPaySdk.setImageResource(resourceId)
    }

    private fun setRightIcon(resourceId: Int) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.layoutRightGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutRightGastroPaySdk.visibility = VISIBLE
        binding.imageToolbarRightGastroPaySdk.setImageResource(resourceId)
    }

    private fun setBackgroundTransparency(transparent: Boolean) {
        binding.rootLayoutToolbarGastroPaySdk.setBackgroundResource(
            if (transparent) {
                android.R.color.transparent
            } else {
                R.color.celtic_gastropay_sdk
            }
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

    fun setTitle(resourceId: Int) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.textTitleGastroPaySdk.visibility = GONE
            return
        }
        binding.imageLogoGastroPaySdk.visibility = GONE
        binding.textTitleGastroPaySdk.visibility = VISIBLE
        binding.textTitleGastroPaySdk.text = context.getText(resourceId)
    }

}