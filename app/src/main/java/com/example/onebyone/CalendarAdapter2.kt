package com.example.onebyone

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter2(private val dataSet: ArrayList<com.example.onebyone.Date>): RecyclerView.Adapter<CalendarAdapter2.ViewHolder>() {
    var drawable: Drawable? = null
    private lateinit var itemClickListener : AdapterView.OnItemClickListener
    val calRecycleEllipse: ImageView? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dateTv: TextView = view.findViewById(R.id.date_cell)
        val dayTv: TextView = view.findViewById(R.id.day_cell)
        val calRecycleEllipse : ImageView = view.findViewById(R.id.cal_recycle_ellipse)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.calendar_cell2, viewGroup, false)

        drawable = ContextCompat.getDrawable(view.context, R.drawable.cal_ellipse)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dateTv.text = dataSet[position].date
        holder.dayTv.text = dataSet[position].day
        Log.d("calendar!!- date",dataSet[position].date)
        Log.d("calendar!!- day",dataSet[position].day)
        Log.d("calendar!!- month",dataSet[position].month)
//        if((dataSet[position].month)==(LocalDate.now().month.toString()).toString()){
//            holder.calRecycleEllipse.visibility = View.VISIBLE
//        } // ** 이거 디베롭해서 오늘 날짜에 초록 표시..
    }

    override fun getItemCount() = dataSet.size
}