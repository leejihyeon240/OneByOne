package com.example.onebyone

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_calendar_popup.*


class CalendarPopup : AppCompatActivity() {

    // temp
    var dailylist = arrayListOf<AddItem>(
        AddItem(R.drawable.btn_food_label, "풀무원국물떡볶이2인", 8900),
        AddItem(R.drawable.btn_food_label, "야채류", 2000),
        AddItem(R.drawable.btn_food_label, "테라갠 500ml*4", 7200)
    )
    val dailylistAdapter = AddRecyclerAdapter(dailylist)
    lateinit var dailyRecyclerView: RecyclerView //***

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