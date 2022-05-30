package com.example.onebyone

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.threetenabp.AndroidThreeTen
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.*
import org.threeten.bp.temporal.ChronoUnit;


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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerView1 : RecyclerView


    val itemList = arrayListOf<Date>()
    val listAdapter = CalendarAdapter2(itemList)
    lateinit var calendarList: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager

    var list = arrayListOf<DailyItem>(
        DailyItem(R.drawable.daily_label_food, "풀무원국물떡볶이2인", "8,900"),
        DailyItem(R.drawable.daily_label_food, "야채류", "2,000"),
        DailyItem(R.drawable.daily_label_drink, "카스캔 355ml*6", "8,900"),
        DailyItem(R.drawable.daily_label_food, "등 / 초밥의 달인", "3,000"),
        DailyItem(R.drawable.daily_label_necessity, "비닐봉투", "100"),
        DailyItem(R.drawable.daily_label_necessity_green, "캐시백", "2,000"),

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_daily, container, false)

        recyclerView1 = rootView.findViewById(R.id.recycler!!)as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        recyclerView1.adapter = DailyRecyclerAdapter(requireContext(),list)


        // horizontal calendar
        calendarList = rootView.findViewById(R.id.daily_calendar_list)
        mLayoutManager = LinearLayoutManager(rootView.context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL // 가로스크롤
        calendarList.layoutManager = mLayoutManager

        var cal_view_date = LocalDate.now()
        setListView(cal_view_date) //***

        return rootView
    }

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



        for(i: Int in 1..lastDayOfMonth) {
            val date = LocalDate.of(date.year, date.month, i)
            val dayOfWeek: DayOfWeek = date.dayOfWeek
            dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)

            itemList.add(Date(dayOfWeek.toString().substring(0, 3), i.toString(),date.month.toString(), date.year.toString()))
        }

        calendarList.adapter = listAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DailyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}