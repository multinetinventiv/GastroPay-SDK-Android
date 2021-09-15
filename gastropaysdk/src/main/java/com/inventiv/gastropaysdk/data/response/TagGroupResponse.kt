package com.inventiv.gastropaysdk.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.ui.common.singleselectiondialog.SingleItemSelectionModel
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class TagGroupResponse(
    @field:SerializedName("id")
    val id: String?,
    @field:SerializedName("tagGroupName")
    val tagGroupName: String?,
    @field:SerializedName("tagGroupKey")
    val tagGroupKey: String?,
    @field:SerializedName("tags")
    val tags: ArrayList<Tag>?
) : Parcelable

@Parcelize
internal data class Tag(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("tagName")
    val tagName: String,
    @field:SerializedName("icon")
    val icon: ImageResponse,
    var isSelected: Boolean = false
) : Parcelable, SingleItemSelectionModel {
    override val title: String
        get() = tagName

    fun isDefaultRegionItem(): Boolean {
        return id == defaultRegionItemId
    }

    fun getRegionTagId(): String? {
        if (isDefaultRegionItem()) {
            return null
        }
        return id
    }

    companion object {
        private const val defaultRegionItemId = "-1"

        fun getDefaultRegionItem(): Tag {
            return Tag(
                defaultRegionItemId,
                StringUtils.getString(R.string.search_merchant_spinner_region_gastropay_sdk),
                ImageResponse(null, null, null, null, null)
            )
        }
    }
}

enum class TagGroupType(val value: String) {
    REGIONS("Regions"), CATEGORIES("Categories"), OTHERS("Others")
}