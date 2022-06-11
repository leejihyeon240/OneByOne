package com.example.onebyone

import android.media.MediaPlayer
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_calendar_popup.*
import java.util.ArrayList


class CalendarPopup : AppCompatActivity() {

    val itemList2 = arrayListOf<DailyData>()
//    val itemList2 = arrayListOf<DailyData>(
//        DailyData("식품", "풀무원국물떡볶이2인", 8900),
//        DailyData("건강/의료", "풀무원국물떡볶이2인", 180),
//        DailyData("식품", "풀무원국물떡볶이2인", 70900),
//        DailyData("홈데코", "홈데코 어쩌구저쩌구", 2180)
//    )
    val dailylistAdapter = DailyAdapter2(itemList2)
    lateinit var dailyRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_popup)

        // sound
        val m: MediaPlayer = MediaPlayer.create(baseContext, R.raw.sound_pop)
        m.start()
        m.setOnCompletionListener { mp ->
            mp.stop()
            mp.release()
        }

        var pyear : String? = intent.getStringExtra("pyear")
        var pmonth : String? = intent.getStringExtra("pmonth")
        var pdate : String? = intent.getStringExtra("pdate")


        var titleList: java.util.ArrayList<String> = intent.getStringArrayListExtra("titleList") as ArrayList<String>
        var priceList: java.util.ArrayList<Int> = intent.getIntegerArrayListExtra("priceList") as ArrayList<Int>

        for (i in 0 until titleList.size){

            itemList2.add(DailyData("식품", titleList[i],priceList[i]))
        }


        var cal_pop_date : TextView = findViewById(R.id.cal_pop_date)
        var cal_pop_empty : ImageView = findViewById(R.id.cal_pop_empty)
        cal_pop_date.text = "$pyear. $pmonth. $pdate"

        dailyRecyclerView = findViewById(R.id.cal_pop_recycler)

        if(titleList.size!=0){

            dailyRecyclerView.adapter = dailylistAdapter
            dailyRecyclerView.visibility = View.VISIBLE
            cal_pop_empty.visibility = View.GONE
        }
        else{
            dailyRecyclerView.visibility = View.GONE
            cal_pop_empty.visibility = View.VISIBLE
        }

        cal_pop_ok.setOnClickListener {
            // sound
            val m: MediaPlayer = MediaPlayer.create(baseContext, R.raw.sound_pop)
            m.start()
            m.setOnCompletionListener { mp ->
                mp.stop()
                mp.release()
            }
            finish()
        }

    }
}