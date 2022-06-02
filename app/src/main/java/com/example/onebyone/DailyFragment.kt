package com.example.onebyone

import android.R.attr.data
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DailyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DailyFragment : Fragment() {


    val itemList = arrayListOf<Date>()
    val listAdapter = CalendarAdapter2(itemList)
    lateinit var dailyCalendarList: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager

    //    val itemList2 = arrayListOf<DailyData>()
    val itemList2 = arrayListOf<DailyData>(
        DailyData("식품", "풀무원국물떡볶이2인", 8900),
        DailyData("건강/의료", "풀무원국물떡볶이2인", 180),
        DailyData("식품", "풀무원국물떡볶이2인", 70900),
        DailyData("홈데코", "홈데코 어쩌구저쩌구", 2180)
    )
    val listAdapter2 = DailyAdapter(itemList2)
    lateinit var dailyContentList: RecyclerView
//    lateinit var mLayoutManager2: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 버튼 클릭 애니메이션
        val anim_buttonclick = AnimationUtils.loadAnimation(activity, R.anim.anim_buttonclick)


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_daily, container, false)
        dailyCalendarList = view.findViewById(R.id.daily_calendar_list) //***
        dailyContentList = view.findViewById(R.id.daily_contenRecycler) //***


        val dailyDateText: TextView = view.findViewById(R.id.daily_titletext)

        var daily_view_date = LocalDate.now()
        dailyDateText.setText(daily_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

//        val extra = arguments
//        if (extra != null) {
//            data = extra.getString("data2")
//        }
//
//        var pyear : String? = intent.getStringExtra("daily_pyear")
//        var pmonth : String? = intent.getStringExtra("daily_pmonth")
//        var pdate : String? = intent.getStringExtra("daily_pdate")
//
//        var daily_dateTv : TextView = view.findViewById(R.id.daily_dateTv)
//        daily_dateTv.text = "$pyear. $pmonth. $pdate"


        // horizontal calendar
        dailyCalendarList = view.findViewById(R.id.daily_calendar_list)
        mLayoutManager = LinearLayoutManager(view.context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL // 가로스크롤
        dailyCalendarList.layoutManager = mLayoutManager

        var cal_leftbutton = view.findViewById(R.id.daily_leftbutton) as ImageView
        var cal_rightbutton = view.findViewById(R.id.daily_rightbutton) as ImageView

        // 왼쪽 버튼 누르면 달 바뀌게 하는거!!!!!!!!!!!!!!
        cal_leftbutton.setOnClickListener {
            daily_view_date = daily_view_date.minusMonths(1)
            Log.d("cal click", daily_view_date.toString())
            setListView(daily_view_date) //***
            dailyDateText.setText(daily_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

            // anim
            cal_leftbutton.startAnimation(anim_buttonclick)

            // sound
            val m: MediaPlayer = MediaPlayer.create(activity, R.raw.sound_pop)
            m.start()
            m.setOnCompletionListener { mp ->
                mp.stop()
                mp.release()
            }


        }

        // 오른쪽 버튼 누르면 달 바뀌게 하는거!!!!!!!!!!!!!!
        cal_rightbutton.setOnClickListener {
            daily_view_date = daily_view_date.plusMonths(1)
            Log.d("cal click", daily_view_date.toString())
            setListView(daily_view_date) //***
            dailyDateText.setText(daily_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

            // anim
            cal_rightbutton.startAnimation(anim_buttonclick)

            // sound
            val m: MediaPlayer = MediaPlayer.create(activity, R.raw.sound_pop)
            m.start()
            m.setOnCompletionListener { mp ->
                mp.stop()
                mp.release()
            }

        }

        setListView(daily_view_date) //***
        setListView2() //***
        return view
    }

    // list(날짜, 요일)를 만들고, adapter를 등록하는 메소드
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListView(date: LocalDate) {

        // 현재 달의 마지막 날짜
//        val lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
//        lastDayOfMonth.format(DateTimeFormatter.ofPattern("dd"))
        itemList.clear()

        val lastDayOfMonth =
            date.with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("dd")).toInt()
//        val calendar = Calendar.getInstance()
//        val date: Date = valueOf(date)
//        val dayOfWeek: Int = date.get(Calendar.DAY_OF_WEEK) - 1


        Log.d("cal clickddddddddddd", date.dayOfWeek.toString())

        for (i: Int in 1..lastDayOfMonth) {
            val date = LocalDate.of(date.year, date.month, i)
            val dayOfWeek: DayOfWeek = date.dayOfWeek
            dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)


            itemList.add(
                Date(
                    dayOfWeek.toString().substring(0, 3),
                    i.toString(),
                    date.month.value.toString(),
                    date.year.toString()
                )
            )

        }
        dailyCalendarList.adapter = listAdapter
    }

    // content 만들고, adapter를 등록하는 메소드
    private fun setListView2() {
        dailyContentList.adapter = listAdapter2
    }
}
