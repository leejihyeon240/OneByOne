package com.example.onebyone

import android.content.Intent
import android.graphics.Color
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
import java.time.LocalDate

class CalendarAdapter(private val dataSet: ArrayList<com.example.onebyone.Date>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    var drawable: Drawable? = null
    private lateinit var itemClickListener : AdapterView.OnItemClickListener

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dateTv: TextView = view.findViewById(R.id.date_cell)
        val dayTv: TextView = view.findViewById(R.id.day_cell)
        val calRecycleEllipse : ImageView = view.findViewById(R.id.cal_recycle_ellipse)
        val outTv: TextView = view.findViewById(R.id.cal_recycle_outtext)
        val inTv: TextView = view.findViewById(R.id.cal_recycle_intext)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.calendar_cell, viewGroup, false)

        drawable = ContextCompat.getDrawable(view.context, R.drawable.cal_ellipse)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dateTv.text = dataSet[position].date
        holder.dayTv.text = dataSet[position].day

        Log.d("calendar!!- 05301",dataSet[position].year.toString())
        Log.d("calendar!!- 05301",dataSet[position].month.toString())
        Log.d("calendar!!- 05301",dataSet[position].date.toString())
        Log.d("calendar!!- 05302",LocalDate.now().year.toString())
        Log.d("calendar!!- 05303",LocalDate.now().month.value.toString())
        Log.d("calendar!!- 05304",LocalDate.now().dayOfMonth.toString())

        if(dataSet[position].year.toString()==LocalDate.now().year.toString()){
            if(dataSet[position].month.toString()==LocalDate.now().month.value.toString()) {
                if(dataSet[position].date.toString()==LocalDate.now().dayOfMonth.toString()) {
                    holder.dateTv.setTextColor(Color.parseColor("#FFFFFF"))
                    holder.calRecycleEllipse.visibility = View.VISIBLE
                }
            }
        }



        //이렇게 간단한 방법이 있는데 왜 그렇게 복잡한 방법을 사용하는거야??? 이곳 저곳에 코드를 잔뜩 집어 넣고??
        holder.itemView.setOnClickListener {

            Log.d("calendar!!- click","뷁")

            val pyear: String = dataSet[position].year //holder로 가져온 값을 변수에 넣기
            val pmonth: String =  dataSet[position].month //holder로 가져온 값을 변수에 넣기
            val pdate: String = dataSet[position].date //holder로 가져온 값을 변수에 넣기

            Log.d("calendar!!- click",pmonth)

            val pintent: Intent = Intent(holder.itemView?.context, MainActivity::class.java) //look_memo.class부분에 원하는 화면 연결

            //변수값 인텐트로 넘기기
            pintent.putExtra("pyear", pyear)
            pintent.putExtra("pmonth", pmonth)
            pintent.putExtra("pdate", pdate)

            ContextCompat.startActivity(holder.itemView?.context, pintent, null) //액티비티 열기

        }



        if(dataSet[position].date==""){
            holder.outTv.text = ""
            holder.inTv.text = ""
        }
        Log.d("calendar!!- date",dataSet[position].date)
        Log.d("calendar!!- day",dataSet[position].day)
        Log.d("calendar!!- month",dataSet[position].month)
//        if((dataSet[position].month)==(LocalDate.now().month.toString()).toString()){
//            holder.calRecycleEllipse.visibility = View.VISIBLE
//        } // ** 이거 디베롭해서 오늘 날짜에 초록 표시..
    }

    override fun getItemCount() = dataSet.size
}