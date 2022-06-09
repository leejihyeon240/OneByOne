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


class CalendarFragment : Fragment()  {
    val itemList = arrayListOf<Date>()
    val listAdapter = CalendarAdapter(itemList)
    lateinit var calendarList: RecyclerView //***
    lateinit var mLayoutManager: LinearLayoutManager



    //firebase 1 -----------------------------------/
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null
    /*-----------------------------------------*/

    // db 관련 애들..
//    var walkdateList: ArrayList<Date> = ArrayList<Date>()
    var calendardata: Map<String, Any>? = null

    var walkdateList: ArrayList<String> = ArrayList<String>()

//    var calendardata: Any? = null

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
        cal_loading.visibility=View.VISIBLE
        Log.d("time1",System.currentTimeMillis().toString())


        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne")
            .child("UserAccount")
        /*-----------------------------------------*/


        // 캘린더데베(1) 활동기록 있는 날짜 리스트로 빼오기!!
        /*-----------------------------------------*/

        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).child("calendar")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    // calendar에 저장된 모든 데이터
                    calendardata = snapshot.getValue() as HashMap<String,  Any>?
                    Log.d("HEY0-calendardata(1)", calendardata.toString())
                    walkdateList.clear() // list 초기화
                    // 키값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    val keys: Set<String> = calendardata!!.keys
                    for (key in keys) {
                        // 활동날짜 리스트에 추가
                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        try {
                            val date22 = formatter.parse(key)
                            //                                date22.setMonth(date22.getMonth()+1);
                            walkdateList.add(date22.toString())
                            Log.d("HEY0-walkdateList+222", walkdateList.toString())
                        } catch (e: Exception) {

                            Log.d("HEY0-walkdateList+222-failed", walkdateList.toString())
                        }
                    }
//
//                    // 캘린더 점 표시
//                    Log.d("HEY", walkdateList.toString())
//                    for (d in walkdateList) {
//                        materialCalendarView.addDecorator(CalEventDecorator(Color.rgb(74, 64, 142), setOf(d)))
//                        Log.d("HEY22222", "dd")
//                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //firebase 3 -----------------------------------/
                    val user = snapshot.getValue(UserAccount::class.java)
                    /*-----------------------------------------*/

                    cal_inputtext.setText(java.lang.String.valueOf(user?.getInput()))
                    cal_outputtext.setText(java.lang.String.valueOf(user?.getOutput()))
                    cal_totaltext.setText(java.lang.String.valueOf((user?.getTotal())))

                    Log.d("time3",System.currentTimeMillis().toString())
                    cal_loading.visibility=View.GONE

                }

                override fun onCancelled(error: DatabaseError) {}
            })




        // 버튼 클릭 애니메이션
        val anim_buttonclick = AnimationUtils.loadAnimation(activity, R.anim.anim_buttonclick)

//        AndroidThreeTen.init(context) // temp

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
            cal_view_date=cal_view_date.plusMonths(1)
            Log.d("cal click",cal_view_date.toString())
            setListView(cal_view_date) //***
            cal_titletext.setText(cal_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())


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



        // 이전 달 마지막 날짜
        val lastDayOfLastMonth = date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())

        val lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("dd")).toInt()
//        val calendar = Calendar.getInstance()
//        val date: Date = valueOf(date)
//        val dayOfWeek: Int = date.get(Calendar.DAY_OF_WEEK) - 1


        Log.d("cal clickddddddddddd",date.dayOfWeek.toString())
        var emptyNum : Int = 0

        when (lastDayOfLastMonth.dayOfWeek.toString()) {
            "SUNDAY" -> emptyNum = 1
            "MONDAY" -> emptyNum = 2
            "TUESDAY" -> emptyNum = 3
            "WEDNESDAY" -> emptyNum = 4
            "THURSDAY" -> emptyNum = 5
            "FRIDAY" -> emptyNum = 6
            "SATURDAY" -> emptyNum = 0
        }

        for (j:Int in 0..emptyNum-1) {
            itemList.add(Date("","","",""))
        }

        for(i: Int in 1..lastDayOfMonth) {
            val date = LocalDate.of(date.year, date.month, i)
            val dayOfWeek: DayOfWeek = date.dayOfWeek
            dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)

//            itemList.add(Date(dayOfWeek.toString().substring(0, 3), i.toString(),date.month.toString(), date.year.toString()))
            itemList.add(Date(dayOfWeek.toString().substring(0, 3), i.toString(),date.month.value.toString(), date.year.toString()))
        }

        calendarList.adapter = listAdapter
    }

}