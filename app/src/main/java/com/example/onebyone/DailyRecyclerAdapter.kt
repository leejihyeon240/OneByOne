package com.example.onebyone

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class DailyRecyclerAdapter(private val context: Context, private val dataList: ArrayList<DailyItem>):
    RecyclerView.Adapter<DailyRecyclerAdapter.MyViewHolder>() {
    var drawable: Drawable? = null

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var titleTv: TextView = itemView.findViewById<TextView>(R.id.titleTv)
        private var priceTv: TextView = itemView.findViewById<TextView>(R.id.priceTv)
        private var dailyLabelFood: ImageView = itemView.findViewById<ImageView>(R.id.dailyLabelFood)

        fun bind(dailyItem: DailyItem, context: Context) {
            titleTv.text = dailyItem.title
            priceTv.text = dailyItem.price
            Glide.with(itemView.context).load(dailyItem.resourceLabelId).into(dailyLabelFood)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.daily_recycler_item, parent, false)

        drawable = ContextCompat.getDrawable(view.context, R.drawable.daily_label_food)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}