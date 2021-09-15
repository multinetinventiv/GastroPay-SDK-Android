package com.inventiv.gastropaysdk.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.databinding.ItemPagingLoadingGastropaySdkBinding
import com.paginate.recycler.LoadingListItemCreator

class CustomLoadingListItemCreator : LoadingListItemCreator {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return LoadingItemViewHolder(
            ItemPagingLoadingGastropaySdkBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

    }

    class LoadingItemViewHolder(
        val binding: ItemPagingLoadingGastropaySdkBinding
    ) : RecyclerView.ViewHolder(binding.root)
}