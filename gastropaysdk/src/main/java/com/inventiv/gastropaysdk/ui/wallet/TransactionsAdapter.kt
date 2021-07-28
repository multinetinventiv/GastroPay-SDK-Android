package com.inventiv.gastropaysdk.ui.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.data.TransactionModel
import com.inventiv.gastropaysdk.databinding.ItemWalletTransactionGastropaySdkBinding

internal class TransactionsAdapter(
    var transactions: MutableList<TransactionModel>,
    private val clickedListener: (merchant: TransactionModel) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = transactions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TransactionItemViewHolder(
            ItemWalletTransactionGastropaySdkBinding.inflate(inflater, parent, false),
            clickedListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransactionItemViewHolder -> holder.bind(transactions[position], position)
        }
    }
}