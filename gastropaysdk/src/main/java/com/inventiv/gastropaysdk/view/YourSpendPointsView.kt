package com.inventiv.gastropaysdk.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.databinding.LayoutYourSpendPointsBinding

class YourSpendPointsView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    interface Listener {
        fun buttonClicked(isSpendSelected : Boolean)
    }

    private lateinit var binding: LayoutYourSpendPointsBinding
    private lateinit var listener: Listener
    private var isSpendSelected = false
    private var availableAmount = 0.0
    private var displayValue = ""

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = LayoutYourSpendPointsBinding.inflate(LayoutInflater.from(context), this, true)
        binding.button.setOnClickListener {
            isSpendSelected = !isSpendSelected
            set()
            listener.buttonClicked(isSpendSelected)
        }
    }

    fun init(listener: Listener, availableAmount: Double, displayValue: String) {
        this.listener = listener
        this.availableAmount = availableAmount
        this.displayValue = displayValue
        set()
    }

    private fun set() {

        val resIdSunglow = ContextCompat.getColor(
            context,
            R.color.sunglow_gastropay_sdk
        )

        val resIdCeltic = ContextCompat.getColor(
            context,
            R.color.celtic_gastropay_sdk
        )

        if (availableAmount <= 0.0) {
            binding.root.visibility = View.GONE
            return
        }

        binding.layoutRoot.visibility = View.VISIBLE
        binding.valueTextView.text = displayValue

        if (isSpendSelected) {
            binding.apply {
                layoutSpendAmount.setBackgroundColor(resIdCeltic)
                pointImageView.setBackgroundResource(R.drawable.ic_point_gastropay_sdk)
                valueTitleTextView.text = "Harcanan TL Puan"
                valueTitleTextView.setTextColor(Color.WHITE)
                valueTextView.setTextColor(resIdSunglow)
                button.setTextColor(resIdSunglow)
                button.setStrokeColorResource(R.color.celtic_gastropay_sdk)
                button.text = "İPTAL"
            }
        } else {
            binding.apply {
                layoutSpendAmount.setBackgroundColor(Color.WHITE)
                pointImageView.setBackgroundResource(R.drawable.ic_point_black_gastropay_sdk)
                valueTitleTextView.text = "Harcayabileceğin TL Puan"
                valueTitleTextView.setTextColor(resIdCeltic)
                valueTextView.setTextColor(resIdCeltic)
                button.setTextColor(resIdCeltic)
                button.setStrokeColorResource(R.color.celtic_gastropay_sdk)
                button.text = "HARCA"
            }
        }
    }
}