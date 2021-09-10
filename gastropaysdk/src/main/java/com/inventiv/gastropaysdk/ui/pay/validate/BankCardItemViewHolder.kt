package com.inventiv.gastropaysdk.ui.pay.validate

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.databinding.ItemBankCardGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.loadImage

internal class BankCardItemViewHolder(
    val binding: ItemBankCardGastropaySdkBinding,
    private val clickedListener: (merchant: BankCardResponse, position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(card: BankCardResponse, position: Int) {
        binding.apply {
            root.setOnClickListener {
                clickedListener.invoke(card, position)
            }

            nameTextView.text = card.alias
            numberTextView.text = card.number
            iconImageView.loadImage(card.bankImageUrl)
            cardTypeImageView.loadImage(card.paymentSourceImageUrl)

            if (card.isDefault) {
                selectedBadgeTextView.visibility = View.VISIBLE
                cardView.setBackgroundResource(R.drawable.background_bank_card_selected_gastropay_sdk)
            } else {
                selectedBadgeTextView.visibility = View.INVISIBLE
                cardView.setBackgroundResource(R.drawable.background_bank_card_unselected_gastropay_sdk)
            }
        }
    }
}