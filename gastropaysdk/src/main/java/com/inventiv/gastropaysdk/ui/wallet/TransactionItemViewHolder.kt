package com.inventiv.gastropaysdk.ui.wallet

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.data.TransactionModel
import com.inventiv.gastropaysdk.databinding.ItemWalletTransactionGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.TransactionType
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

            when (transaction.transactionType) {
                TransactionType.DEPOSIT -> {
                    typeTextView.text = StringUtils.getString(R.string.wallet_earn_gastropay_sdk)
                    typeTextView.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.shamrock_gastropay_sdk
                        )
                    )
                }
                TransactionType.WITHDRAW -> {
                    typeTextView.text = StringUtils.getString(R.string.wallet_spend_gastropay_sdk)
                    typeTextView.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.reddish_orange_gastropay_sdk
                        )
                    )
                }
                TransactionType.CANCEL_DEPOSIT,
                TransactionType.CANCEL_WITHDRAW -> {
                    typeTextView.text = StringUtils.getString(R.string.wallet_cancel_gastropay_sdk)
                    typeTextView.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.suva_grey_gastropay_sdk
                        )
                    )
                }
            }

            if (position % 2 == 0) {
                rootLayout.setBackgroundColor(Color.parseColor("#F1FFF7"))
            } else {
                rootLayout.setBackgroundColor(Color.WHITE)
            }

        }
    }
}