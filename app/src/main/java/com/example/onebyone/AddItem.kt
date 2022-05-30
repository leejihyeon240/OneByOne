package com.example.onebyone

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddItem(private val resourceLabelId: Int, var title: String, var price: Int, var type: Int = 0) : Parcelable {
    var isChecked = false

    override fun toString(): String {
        return "AddItem(isChecked=$isChecked, resourceLabelId=$resourceLabelId, title='$title', price='$price')"
    }
}