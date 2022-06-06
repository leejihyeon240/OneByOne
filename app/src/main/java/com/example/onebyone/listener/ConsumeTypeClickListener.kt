package com.example.onebyone.listener

import com.example.onebyone.ConsumeItem

interface ConsumeTypeClickListener {
    fun onSelect(data: List<ConsumeItem>, position: Int)
}