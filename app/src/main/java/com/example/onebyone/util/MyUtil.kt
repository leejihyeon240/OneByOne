package com.example.onebyone.util

import java.lang.Exception
import java.text.NumberFormat
import java.util.*

fun Int.toWon() = NumberFormat.getNumberInstance(Locale.US).format(this)

fun String.toDigit() = try {
    this.replace(",", "").replace("원", "").toInt()
}catch (e: Exception){
    -1
}