package com.inventiv.gastropaysdk.ui.merchants.searchmerchant

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.data.response.Tag
import com.inventiv.gastropaysdk.databinding.ItemTagGroupGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.COMMA
import com.inventiv.gastropaysdk.utils.EMPTY
import com.inventiv.gastropaysdk.utils.loadImage

internal class TagAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val tags = arrayListOf<Tag>()

    fun setTags(tags: List<Tag>) {
        this.tags.clear()
        this.tags.addAll(tags)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TagGroupViewHolder(
            ItemTagGroupGastropaySdkBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TagGroupViewHolder -> holder.bind(tags[position], position)
        }
    }

    fun getSelectedTags(): List<Tag> = tags.filter { it.isSelected }

    fun getTagIds(): String {
        val selectedTags = tags.filter { it.isSelected }
        if (selectedTags.isEmpty()) {
            return EMPTY
        }
        val strBuilder = StringBuilder()
        selectedTags
            .map { it.id }
            .forEach {
                strBuilder.append(it)
                strBuilder.append(COMMA)
            }
        val strIds = strBuilder.toString()
        return strIds.substring(0, strIds.length - 1)
    }

    fun clearTags() {
        tags.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }
}

internal class TagGroupViewHolder(
    val binding: ItemTagGroupGastropaySdkBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(tag: Tag, position: Int) {
        changeCardColor(tag.isSelected)
        binding.cardRoot.setOnClickListener {
            tag.isSelected = !tag.isSelected
            changeCardColor(tag.isSelected)
        }
        binding.imageTagIcon.loadImage(tag.icon.url)
        binding.textTagTitle.text = tag.tagName
    }

    private fun changeCardColor(isSelected: Boolean) {
        if (isSelected) {
            binding.cardRoot.setCardBackgroundColor(
                ContextCompat.getColor(
                    binding.cardRoot.context,
                    R.color.sunglow_gastropay_sdk
                )
            )
        } else {
            binding.cardRoot.setCardBackgroundColor(Color.WHITE)
        }
    }
}