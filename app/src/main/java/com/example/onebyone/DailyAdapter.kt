package com.example.onebyone

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DailyAdapter(private val dataList: ArrayList<DailyData>) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    var drawable: Drawable? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTv: TextView = itemView.findViewById<TextView>(R.id.daily_cell_category)
        val titleTv: TextView = itemView.findViewById<TextView>(R.id.daily_cell_title)
        val priceTv: TextView = itemView.findViewById<TextView>(R.id.daily_cell_price)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.daily_cell, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryTv.text = dataList[position].category
        holder.titleTv.text = dataList[position].title
        holder.priceTv.text = dataList[position].price.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
