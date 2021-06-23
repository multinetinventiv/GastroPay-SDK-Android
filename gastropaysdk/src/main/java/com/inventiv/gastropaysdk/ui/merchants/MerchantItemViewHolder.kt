package com.inventiv.gastropaysdk.ui.merchants

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inventiv.gastropaysdk.data.model.response.Merchant
import com.inventiv.gastropaysdk.databinding.ItemMerchantGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.getDistanceAsMeters

internal class MerchantItemViewHolder(
    val binding: ItemMerchantGastropaySdkBinding,
    private val clickedListener: (merchant: Merchant) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(merchant: Merchant) {
        binding.apply {
            root.setOnClickListener {
                clickedListener.invoke(merchant)
            }

            nameTextViewGastroPaySdk.text = merchant.name
            distanceTextViewGastroPaySdk.text =
                binding.root.context.getDistanceAsMeters(merchant.distance)
            priceTextViewGastroPaySdk.text = merchant.rewardPercentage
            Glide.with(binding.root.context).load(merchant.showcaseImageUrl)
                .into(mainImageViewGastroPaySdk)

            if (merchant.isBonusPoint == true) {
                imageBonusGastroPaySdk.visibility = VISIBLE
            } else {
                imageBonusGastroPaySdk.visibility = GONE
            }
        }
    }
}