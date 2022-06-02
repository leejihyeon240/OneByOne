package com.example.onebyone

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_calendar_popup.*


class CalendarPopup : AppCompatActivity() {

    //    val itemList2 = arrayListOf<DailyData>()
    val itemList2 = arrayListOf<DailyData>(
        DailyData("식품", "풀무원국물떡볶이2인", 8900),
        DailyData("건강/의료", "풀무원국물떡볶이2인", 180),
        DailyData("식품", "풀무원국물떡볶이2인", 70900),
        DailyData("홈데코", "홈데코 어쩌구저쩌구", 2180)
    )
    val dailylistAdapter = DailyAdapter2(itemList2)
    lateinit var dailyRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_popup)

        var pyear : String? = intent.getStringExtra("pyear")
        var pmonth : String? = intent.getStringExtra("pmonth")
        var pdate : String? = intent.getStringExtra("pdate")

        var cal_pop_date : TextView = findViewById(R.id.cal_pop_date)
        cal_pop_date.text = "$pyear. $pmonth. $pdate"

        dailyRecyclerView = findViewById(R.id.cal_pop_recycler)
        dailyRecyclerView.adapter = dailylistAdapter

        cal_pop_ok.setOnClickListener {
            finish()
        }

    }
}