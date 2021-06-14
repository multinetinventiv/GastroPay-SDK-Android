package com.inventiv.gastropaysdk.ui.merchants

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

            nameTextView.text = merchant.name
            distanceTextView.text = binding.root.context.getDistanceAsMeters(merchant.distance)
            priceTextView.text = merchant.rewardPercentage
            Glide.with(binding.root.context).load(merchant.showcaseImageUrl).into(mainImageView)
        }
    }
}