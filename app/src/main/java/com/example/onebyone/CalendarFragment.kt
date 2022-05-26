package com.example.onebyone

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.RoundingMode.valueOf
import java.sql.Date.valueOf
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CalendarFragment : Fragment()  {
    val itemList = arrayListOf<Date>()
    val listAdapter = CalendarAdapter(itemList)
    lateinit var calendarList: RecyclerView //***
    lateinit var mLayoutManager: LinearLayoutManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        AndroidThreeTen.init(context) // temp

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarList = view.findViewById(R.id.calendar_list) //***
//        mLayoutManager = LinearLayoutManager(view.context)
        val cal_titletext : TextView = view.findViewById(R.id.cal_titletext)

        var cal_view_date = LocalDate.now()
        cal_titletext.setText(cal_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

        var cal_leftbutton = view.findViewById(R.id.cal_leftbutton) as ImageView
        var cal_rightbutton = view.findViewById(R.id.cal_rightbutton) as ImageView

        // 왼쪽 버튼 누르면 달 바뀌게 하는거!!!!!!!!!!!!!!
        cal_leftbutton.setOnClickListener {
            cal_view_date=cal_view_date.minusMonths(1)
            Log.d("cal click",cal_view_date.toString())
            setListView(cal_view_date) //***
            cal_titletext.setText(cal_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())
        }

        // 오른쪽 버튼 누르면 달 바뀌게 하는거!!!!!!!!!!!!!!
        cal_rightbutton.setOnClickListener {
            cal_view_date=cal_view_date.plusMonths(1)
            Log.d("cal click",cal_view_date.toString())
            setListView(cal_view_date) //***
            cal_titletext.setText(cal_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())
        }

//        // recyclerView orientation (가로 방향 스크롤 설정)
//        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        calendarList.layoutManager = mLayoutManager



        setListView(cal_view_date) //***
        Log.d("cal1",cal_view_date.toString())
        Log.d("cal2",cal_view_date.minusMonths(2).toString())
        return view
    }

//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.cal_leftbutton -> {
//                cal_view_date.minusMonths(1)
//                print(cal_view_date)
//            }
//        }
//    }


    // list(날짜, 요일)를 만들고, adapter를 등록하는 메소드
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListView(date : LocalDate) {

        // 현재 달의 마지막 날짜
//        val lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
//        lastDayOfMonth.format(DateTimeFormatter.ofPattern("dd"))
        itemList.clear()

        val lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("dd")).toInt()
//        val calendar = Calendar.getInstance()
//        val date: Date = valueOf(date)
//        val dayOfWeek: Int = date.get(Calendar.DAY_OF_WEEK) - 1


        Log.d("cal clickddddddddddd",date.dayOfWeek.toString())
        var emptyNum : Int = 0

        when (date.dayOfWeek.toString()) {
            "SUNDAY" -> emptyNum = 0
            "MONDAY" -> emptyNum = 1
            "TUESDAY" -> emptyNum = 2
            "WEDNESDAY" -> emptyNum = 3
            "THURSDAY" -> emptyNum = 4
            "FRIDAY" -> emptyNum = 5
            "SATURDAY" -> emptyNum = 6
        }

        for (j:Int in 0..emptyNum-1) {
            itemList.add(Date("","","",""))
        }

        for(i: Int in 1..lastDayOfMonth) {
            val date = LocalDate.of(date.year, date.month, i)
            val dayOfWeek: DayOfWeek = date.dayOfWeek
            dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)

            itemList.add(Date(dayOfWeek.toString().substring(0, 3), i.toString(),date.month.toString(), date.year.toString()))
        }

        calendarList.adapter = listAdapter
    }

}