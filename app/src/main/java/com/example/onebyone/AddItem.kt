package com.example.onebyone

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddItem(val resourceLabelId: Int, var title: String, var price: Int, var type: Int = 0) : Parcelable {
    var isChecked = false

    override fun toString(): String {
        return "AddItem(isChecked=$isChecked, resourceLabelId=$resourceLabelId, title='$title', price='$price')"
    }

    fun getTitleHyerm(): String {
        return title
    }

    fun getPriceHyerm(): Int {
        return price
    }

    fun getTypeHyerm(): Int {
        return type
    }

}
