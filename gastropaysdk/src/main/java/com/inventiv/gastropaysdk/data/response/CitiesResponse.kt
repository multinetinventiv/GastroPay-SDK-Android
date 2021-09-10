package com.inventiv.gastropaysdk.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseResponse
import com.inventiv.gastropaysdk.ui.common.singleselectiondialog.SingleItemSelectionModel
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils
import kotlinx.parcelize.Parcelize

internal data class CitiesResponse(
    @field:SerializedName("cities")
    val cities: ArrayList<City>,
) : BaseResponse()

@Parcelize
internal data class City(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
) : Parcelable, SingleItemSelectionModel {
    override val title: String
        get() = name

    fun isDefaultItem(): Boolean {
        return id == defaultItemId
    }

    companion object {
        private const val defaultItemId = -1

        fun getDefaultItem(): City {
            return City(
                defaultItemId,
                StringUtils.getString(R.string.search_merchant_spinner_city_gastropay_sdk)
            )
        }
    }
}