package com.example.onebyone

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.DecimalFormat
import java.text.NumberFormat
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


class CalendarFragment : Fragment() {
    var cal_view_date = LocalDate.now()

    val itemList = arrayListOf<Date>()
    val listAdapter = CalendarAdapter(itemList)
    lateinit var calendarList: RecyclerView //***
    lateinit var mLayoutManager: LinearLayoutManager


    //firebase 1 -----------------------------------/
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null
    /*-----------------------------------------*/

    // db 관련 애들..

    var dbDataset: Map<*, *>? = null
    var calendardata: Map<String, Any>? = null
    var tvDateList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var titleList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var priceList: java.util.ArrayList<Int> = java.util.ArrayList<Int>()


    // 리사이클러뷰에 표시할 데이터 리스트 생성.
    var startlist = ArrayList<String>()
    var endlist = ArrayList<String>()
    var steplist = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        var cal_loading = view.findViewById(R.id.cal_loading) as ImageView
        cal_loading.visibility = View.VISIBLE


        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne")
            .child("UserAccount")
        /*-----------------------------------------*/


        /*-----------------------------------------*/
        // 캘린더데베(1) 활동기록 있는 날짜 리스트로 빼오기!!
        /*-----------------------------------------*/

        updateNum()


        // 지워야 하는 애들 -  User 어쩌구에서도 수정해야 함
//        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    //firebase 3 -----------------------------------/
//                    val user = snapshot.getValue(UserAccount::class.java)
//                    /*-----------------------------------------*/
//
//                    cal_inputtext.setText(java.lang.String.valueOf(user?.getInput()))
//                    cal_outputtext.setText(java.lang.String.valueOf(user?.getOutput()))
//                    cal_totaltext.setText(java.lang.String.valueOf((user?.getTotal())))
//
//                    Log.d("time3",System.currentTimeMillis().toString())
//                    cal_loading.visibility=View.GONE
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })


        // 버튼 클릭 애니메이션
        val anim_buttonclick = AnimationUtils.loadAnimation(activity, R.anim.anim_buttonclick)

//        AndroidThreeTen.init(context) // temp

        calendarList = view.findViewById(R.id.calendar_list) //***
//        mLayoutManager = LinearLayoutManager(view.context)
        val cal_titletext: TextView = view.findViewById(R.id.cal_titletext)


        cal_titletext.setText(cal_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

        var cal_leftbutton = view.findViewById(R.id.cal_leftbutton) as ImageView
        var cal_rightbutton = view.findViewById(R.id.cal_rightbutton) as ImageView

        // 왼쪽 버튼 누르면 달 바뀌게 하는거!!!!!!!!!!!!!!
        cal_leftbutton.setOnClickListener {
            cal_view_date = cal_view_date.minusMonths(1)
            setListView(cal_view_date) //***
            cal_titletext.setText(cal_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())
            updateNum()

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
            cal_view_date = cal_view_date.plusMonths(1)
            setListView(cal_view_date) //***
            cal_titletext.setText(cal_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())
            updateNum()


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

//        // recyclerView orientation (가로 방향 스크롤 설정)
//        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        calendarList.layoutManager = mLayoutManager


        setListView(cal_view_date) //***

//        cal_inputtext.setText("0")
//        cal_outputtext.setText("0")
//        cal_totaltext.setText("0")

        cal_loading.visibility = View.GONE

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
    private fun setListView(date: LocalDate) {

        // 현재 달의 마지막 날짜
//        val lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
//        lastDayOfMonth.format(DateTimeFormatter.ofPattern("dd"))
        itemList.clear()


        // 이전 달 마지막 날짜
        val lastDayOfLastMonth = date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())

        val lastDayOfMonth =
            date.with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("dd")).toInt()
//        val calendar = Calendar.getInstance()
//        val date: Date = valueOf(date)
//        val dayOfWeek: Int = date.get(Calendar.DAY_OF_WEEK) - 1


        var emptyNum: Int = 0

        when (lastDayOfLastMonth.dayOfWeek.toString()) {
            "SUNDAY" -> emptyNum = 1
            "MONDAY" -> emptyNum = 2
            "TUESDAY" -> emptyNum = 3
            "WEDNESDAY" -> emptyNum = 4
            "THURSDAY" -> emptyNum = 5
            "FRIDAY" -> emptyNum = 6
            "SATURDAY" -> emptyNum = 0
        }

        for (j: Int in 0..emptyNum - 1) {
            itemList.add(Date("", "", "", ""))
        }

        for (i: Int in 1..lastDayOfMonth) {
            val date = LocalDate.of(date.year, date.month, i)
            val dayOfWeek: DayOfWeek = date.dayOfWeek
            dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)

//            itemList.add(Date(dayOfWeek.toString().substring(0, 3), i.toString(),date.month.toString(), date.year.toString()))
            itemList.add(
                Date(
                    dayOfWeek.toString().substring(0, 3),
                    i.toString(),
                    date.month.value.toString(),
                    date.year.toString()
                )
            )
        }

        calendarList.adapter = listAdapter
    }

    private fun updateNum() {
        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).child("calendar")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

//                    cal_outputtext.setText("0")

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
                                    Log.d("HEY cal i", i.toString())



//                                    priceList.clear()

                                    // 해당 날짜에 기록된 모든 데이터
                                    dbDataset = null
                                    dbDataset =
                                        (calendardata as HashMap<String, Any>).get(tvDateList[i]) as Map<*, *>?


                                    Log.d("HEY cal dbDataset", dbDataset.toString())

//                                    if (dbDataset != null && !dbDataset!!.isEmpty()) {
                                    // 각각의 활동기록 하나씩 반복구문 돌아돌아빙글뱅글어지러워~

                                    // 혜림아 샹궈 먹고 여기부터 수정해

                                    for (i in dbDataset!!.values.toTypedArray().indices) {
                                        // 해당 날짜에 기록된 첫 번째 데이터
                                        val data1 = dbDataset!!.values.toTypedArray()[i]!!
                                        val data2: Map<String, Any> = data1 as HashMap<String, Any> //재가공(형변환)

                                        // 혜림아 샹궈 먹고 여기부터 수정해
                                        pricesum += data2.get("price").toString().toInt()

                                        Log.d("HEY cal price", data2.get("price").toString())
                                        Log.d("HEY cal pricenum2", pricesum.toString())
//                                    priceList.add(data2.get("price").toString().toInt())

                                    }

//                                    }

                                }
                            }

                        } catch (e: Exception) {
                            Log.d("HEY cal failed", tvDateList.toString())
                        }


//                    Log.d("HEY cal priceList", priceList.toString())
//                    Log.d("HEY cal priceList sum", priceList.sum().toString())


//                    cal_outputtext.setText(NumberFormat.getInstance(Locale.KOREA).format(pricesum))
                }


                override fun onCancelled(error: DatabaseError) {}
            })


    }

}