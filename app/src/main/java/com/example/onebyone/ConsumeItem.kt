package com.example.onebyone

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import kotlinx.android.parcel.Parcelize

@Parcelize
class ConsumeItem(@DrawableRes val resId: Int, val name: String) : Parcelable {
    var isSelected = false

    override fun toString(): String {
        return "ConsumeItem(resId=$resId, name='$name', isSelected=$isSelected)"
    }
}