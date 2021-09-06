package com.inventiv.gastropaysdk.ui.pay.validate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.databinding.ItemBankCardGastropaySdkBinding

internal class BankCardsAdapter(
    var cards: MutableList<BankCardResponse>,
    private val clickedListener: (card: BankCardResponse, position : Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = cards.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BankCardItemViewHolder(
            ItemBankCardGastropaySdkBinding.inflate(inflater, parent, false),
            clickedListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BankCardItemViewHolder -> holder.bind(cards[position], position)
        }
    }
}