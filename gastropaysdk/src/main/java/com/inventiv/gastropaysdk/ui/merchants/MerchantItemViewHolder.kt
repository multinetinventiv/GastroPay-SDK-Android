package com.inventiv.gastropaysdk.ui.merchants

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inventiv.gastropaysdk.databinding.ItemMerchantGastropaySdkBinding

internal class MerchantItemViewHolder(
    val binding: ItemMerchantGastropaySdkBinding,
    private val clickedListener: (merchant: MerchantsFragment.TestMerchant) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(merchant: MerchantsFragment.TestMerchant) {
        binding.apply {
            root.setOnClickListener {
                clickedListener.invoke(merchant)
            }

            nameTextView.text = merchant.name
            distanceTextView.text = merchant.distance
            priceTextView.text = "%10"
            Glide.with(binding.root.context).load(merchant.image).into(mainImageView)
        }
    }
}