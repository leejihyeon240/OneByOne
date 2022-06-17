package com.example.onebyone

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddItem(val resourceLabelId: Int,var title: String, var price: Int, var category: String) : Parcelable {
    var isChecked = false

    override fun toString(): String {
        return "AddItem(isChecked=$isChecked, title='$title', price='$price')"
    }

    fun getTitleHyerm(): String {
        return title
    }

    fun getPriceHyerm(): Int {
        return price
    }

    fun getCategoryHyerm(): String {
        return category
    }
}
