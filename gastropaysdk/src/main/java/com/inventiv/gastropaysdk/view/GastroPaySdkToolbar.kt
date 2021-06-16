package com.inventiv.gastropaysdk.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.databinding.ViewToolbarGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.REFERENCE_UNDEFINED

internal class GastroPaySdkToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private val binding: ViewToolbarGastropaySdkBinding = ViewToolbarGastropaySdkBinding.inflate(
        LayoutInflater.from(context),
        this,
        false
    )

    init {
        addView(binding.root)
        val a = context.obtainStyledAttributes(attrs, R.styleable.GastroPaySdkToolbar)
        try {
            var title = a.getString(R.styleable.GastroPaySdkToolbar_gastropay_sdk_toolbar_title)
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
                binding.layoutToolbarGastroPaySdk.imageLogoGastroPaySdk.visibility = GONE
                binding.layoutToolbarGastroPaySdk.textTitleGastroPaySdk.visibility = GONE
            }

            if (leftIconId != REFERENCE_UNDEFINED) {
                setLeftIcon(leftIconId)
            } else {
                binding.layoutToolbarGastroPaySdk.layoutLeftGastroPaySdk.visibility = GONE
            }

            if (rightIconId != REFERENCE_UNDEFINED) {
                setRightIcon(rightIconId)
            } else {
                binding.layoutToolbarGastroPaySdk.layoutRightGastroPaySdk.visibility = GONE
            }

            if (isTransparentBg) {
                setBackgroundTransparency(true)
            }

        } finally {
            a.recycle()
        }
    }

    fun onLeftIconClick(operation: () -> Unit) {
        binding.layoutToolbarGastroPaySdk.layoutLeftGastroPaySdk.setOnClickListener {
            operation()
        }
    }

    fun onRightIconClick(operation: () -> Unit) {
        binding.layoutToolbarGastroPaySdk.layoutRightGastroPaySdk.setOnClickListener {
            operation()
        }
    }

    fun setCenterImage(resourceId: Int) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.layoutToolbarGastroPaySdk.imageLogoGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutToolbarGastroPaySdk.imageLogoGastroPaySdk.visibility = VISIBLE
        binding.layoutToolbarGastroPaySdk.imageLogoGastroPaySdk.setImageResource(resourceId)
        binding.layoutToolbarGastroPaySdk.textTitleGastroPaySdk.visibility = GONE
    }

    fun setLeftIcon(resourceId: Int) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.layoutToolbarGastroPaySdk.layoutLeftGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutToolbarGastroPaySdk.layoutLeftGastroPaySdk.visibility = VISIBLE
        binding.layoutToolbarGastroPaySdk.imageToolbarLeftGastroPaySdk.setImageResource(resourceId)
    }

    fun setRightIcon(resourceId: Int) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.layoutToolbarGastroPaySdk.layoutRightGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutToolbarGastroPaySdk.layoutRightGastroPaySdk.visibility = VISIBLE
        binding.layoutToolbarGastroPaySdk.imageToolbarRightGastroPaySdk.setImageResource(resourceId)
    }

    fun setBackgroundTransparency(transparent: Boolean) {
        binding.layoutToolbarGastroPaySdk.rootLayoutToolbarGastroPaySdk.setBackgroundResource(
            if (transparent) {
                android.R.color.transparent
            } else {
                R.color.celtic_gastropay_sdk
            }
        )
    }

    fun setTitle(title: String) {
        if (title.isEmpty()) {
            binding.layoutToolbarGastroPaySdk.textTitleGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutToolbarGastroPaySdk.imageLogoGastroPaySdk.visibility = GONE
        binding.layoutToolbarGastroPaySdk.textTitleGastroPaySdk.visibility = VISIBLE
        binding.layoutToolbarGastroPaySdk.textTitleGastroPaySdk.text = title
    }

    override fun setTitle(resourceId: Int) {
        if (resourceId == REFERENCE_UNDEFINED) {
            binding.layoutToolbarGastroPaySdk.textTitleGastroPaySdk.visibility = GONE
            return
        }
        binding.layoutToolbarGastroPaySdk.imageLogoGastroPaySdk.visibility = GONE
        binding.layoutToolbarGastroPaySdk.textTitleGastroPaySdk.visibility = VISIBLE
        binding.layoutToolbarGastroPaySdk.textTitleGastroPaySdk.text = context.getText(resourceId)
    }

}