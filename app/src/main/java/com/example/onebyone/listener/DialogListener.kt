package com.example.onebyone.listener

import com.example.onebyone.AddItem

interface DialogListener{
    fun onAdd(item: AddItem)
    fun onDelete(position: Int)
    fun onUpdate(position: Int, item: AddItem)
}