package com.example.onebyone.listener

import com.example.onebyone.AddItem

interface ItemClickListener{
    fun onClick(dataList: List<AddItem>, position: Int)
}