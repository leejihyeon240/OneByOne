package com.example.onebyone

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_daily.*
import java.text.SimpleDateFormat
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
class DailyFragment : Fragment()  {

    var cal_view_date = LocalDate.now()


    val itemList = arrayListOf<Date>()
    val listAdapter = CalendarAdapter2(itemList)
    lateinit var dailyCalendarList: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager


    var titleListPop: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var priceListPop: java.util.ArrayList<Int> = java.util.ArrayList<Int>()


    //firebase 1 -----------------------------------/
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null
    /*-----------------------------------------*/

    // db 관련 애들..
    // db 관련 애들..


    var titleList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var priceList: java.util.ArrayList<Int> = java.util.ArrayList<Int>()

    var dbDataset: Map<*, *>? = null
    var calendardata: Map<String, Any>? = null
    var tvDateList: java.util.ArrayList<String> = java.util.ArrayList<String>()

    var pyear : String = ""
    var pmonth: String = ""
    var pdate : String = ""

    //    val itemList2 = arrayListOf<DailyData>()



    var itemList2 = arrayListOf<DailyData>()
    val listAdapter2 = DailyAdapter(itemList2)
    lateinit var dailyContentList: RecyclerView
    lateinit var mLayoutManager2: LinearLayoutManager








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_daily, container, false)

        var daily_loading = view.findViewById(R.id.daily_loading) as ImageView
        daily_loading.visibility=View.VISIBLE
        Log.d("time1",System.currentTimeMillis().toString())



        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne")
            .child("UserAccount")
        /*-----------------------------------------*/



        // 버튼 클릭 애니메이션
        val anim_buttonclick = AnimationUtils.loadAnimation(activity, R.anim.anim_buttonclick)


        // Inflate the layout for this fragment
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

        Log.d("HEY daily itemList", itemList.toString())
        Log.d("HEY daily itemList size", itemList.size.toString())

        var cal_leftbutton = view.findViewById(R.id.daily_leftbutton) as ImageView
        var cal_rightbutton = view.findViewById(R.id.daily_rightbutton) as ImageView


        updateNum()

        // 왼쪽 버튼 누르면 달 바뀌게 하는거!!!!!!!!!!!!!!
        cal_leftbutton.setOnClickListener {
            daily_view_date = daily_view_date.minusMonths(1)
            Log.d("cal click", daily_view_date.toString())
            setListView(daily_view_date) //***
            dailyDateText.setText(daily_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())



            Log.d("HEY daily itemList", itemList.toString())
            Log.d("HEY daily itemList size", itemList.size.toString())

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

        daily_loading.visibility=View.GONE
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


    private fun updateNum() {
        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).child("calendar")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    daily_outTv.setText("0")

//                    dbDataset = null
//                    priceList.clear()
                    var pricesum: Int = 0
                    Log.d("HEY cal pricenum1", pricesum.toString())

                    // calendar에 저장된 모든 데이터
                    calendardata = snapshot.getValue() as HashMap<String, Any>?
                    tvDateList.clear() // list 초기화
                    // 키값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    val keys: Set<String> = calendardata!!.keys
                    for (key in keys) {
                        tvDateList.add(key)
                    }
                    // 활동날짜 리스트에 추가
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    try {



                        // 만약 기록이 있는 날이라면
                        for (i: Int in 0 until tvDateList.size) {

                            Log.d("HEY cal tvDateList", tvDateList.toString())
                            Log.d("HEY cal tvDateList[i]", tvDateList[i])

//                                if(cal_view_date.year == tvDateList[i].split("-")[0].toInt() &&
//                                    cal_view_date.month == tvDateList[i].split("-")[1].toInt())


                            if (cal_view_date.year == tvDateList[i].split("-")[0].toInt() &&
                                cal_view_date.monthValue == tvDateList[i].split("-")[1].toInt()
                            ) {
                                Log.d("HEY daily i", i.toString())



//                                    priceList.clear()

                                // 해당 날짜에 기록된 모든 데이터
                                dbDataset = null
                                dbDataset =
                                    (calendardata as HashMap<String, Any>).get(tvDateList[i]) as Map<*, *>?


                                Log.d("HEY daily dbDataset", dbDataset.toString())

//                                    if (dbDataset != null && !dbDataset!!.isEmpty()) {
                                // 각각의 활동기록 하나씩 반복구문 돌아돌아빙글뱅글어지러워~

                                // 혜림아 샹궈 먹고 여기부터 수정해

                                for (i in dbDataset!!.values.toTypedArray().indices) {
                                    // 해당 날짜에 기록된 첫 번째 데이터
                                    val data1 = dbDataset!!.values.toTypedArray()[i]!!
                                    val data2: Map<String, Any> = data1 as HashMap<String, Any> //재가공(형변환)

                                    // 혜림아 샹궈 먹고 여기부터 수정해
                                    pricesum += data2.get("price").toString().toInt()

                                    Log.d("HEY daily price", data2.get("price").toString())
                                    Log.d("HEY daily pricenum2", pricesum.toString())
//                                    priceList.add(data2.get("price").toString().toInt())

                                }

//                                    }

                            }
                        }

                    } catch (e: Exception) {
                        Log.d("HEY daily failed", tvDateList.toString())
                    }


//                    Log.d("HEY cal priceList", priceList.toString())
//                    Log.d("HEY cal priceList sum", priceList.sum().toString())


                    daily_outTv.setText(pricesum.toString())
                }


                override fun onCancelled(error: DatabaseError) {}
            })


    }

}
