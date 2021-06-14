package com.inventiv.gastropaysdk.ui.merchants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.data.model.response.Merchant
import com.inventiv.gastropaysdk.databinding.ItemMerchantGastropaySdkBinding

internal class MerchantAdapter(
    var merchants: MutableList<Merchant>,
    private val clickedListener: (merchant: Merchant) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = merchants.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MerchantItemViewHolder(
            ItemMerchantGastropaySdkBinding.inflate(inflater, parent, false),
            clickedListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MerchantItemViewHolder -> holder.bind(merchants[position])
        }
    }
}