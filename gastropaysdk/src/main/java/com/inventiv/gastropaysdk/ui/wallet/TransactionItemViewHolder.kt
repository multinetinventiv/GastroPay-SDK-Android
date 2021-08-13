package com.inventiv.gastropaysdk.ui.wallet

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.data.TransactionModel
import com.inventiv.gastropaysdk.databinding.ItemWalletTransactionGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils
import com.inventiv.gastropaysdk.utils.formatDate


internal class TransactionItemViewHolder(
    val binding: ItemWalletTransactionGastropaySdkBinding,
    private val clickedListener: (merchant: TransactionModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(transaction: TransactionModel, position: Int) {
        binding.apply {
            root.setOnClickListener {
                clickedListener.invoke(transaction)
            }

            nameTextView.text = transaction.name
            valueTextView.text = transaction.price
            dateTextView.text = transaction.date.formatDate("dd.MM.yyyy - HH:mm")

            if (transaction.isEarn) {
                typeTextView.text =
                    StringUtils.getString(R.string.wallet_earn_gastropay_sdk)
                typeTextView.setTextColor(Color.parseColor("#2ECC71"))
            } else {
                typeTextView.text =
                    StringUtils.getString(R.string.wallet_spend_gastropay_sdk)
                typeTextView.setTextColor(Color.parseColor("#FD3850"))
            }

            if (position % 2 == 0) {
                rootLayout.setBackgroundColor(Color.parseColor("#F1FFF7"))
            } else {
                rootLayout.setBackgroundColor(Color.WHITE)
            }

        }
    }
}