package com.inventiv.gastropaysdk.ui.common.singleselectiondialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.databinding.ItemSingleSelectionGastropaySdkBinding

internal class SingleItemSelectionAdapter<T : SingleItemSelectionModel>(
    private val itemList: ArrayList<T>,
    private val itemClick: (item: T) -> Unit
) : RecyclerView.Adapter<SingleItemSelectionAdapter.ViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemSingleSelectionGastropaySdkBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(itemList[position], itemClick)
    }

    override fun getItemCount(): Int = itemList.size

    class ViewHolder<T : SingleItemSelectionModel>(
        val binding: ItemSingleSelectionGastropaySdkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T, itemClick: (item: T) -> Unit) {
            binding.textItemTitle.text = item.title

            binding.root.setOnClickListener {
                itemClick.invoke(item)
            }
        }
    }
}