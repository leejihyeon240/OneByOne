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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Objects

class CalendarAdapter(private val dataSet: ArrayList<com.example.onebyone.Date>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    var drawable: Drawable? = null
    private lateinit var itemClickListener : AdapterView.OnItemClickListener

    //firebase 1 -----------------------------------/
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null
    /*-----------------------------------------*/

    // db 관련 애들..

    var dbDataset:Map<*, *>? = null

    var calendardata: Map<String, Any>? = null
    var tvDateList: java.util.ArrayList<String> = java.util.ArrayList<String>()


    var titleList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var priceList: java.util.ArrayList<Int> = java.util.ArrayList<Int>()


    var titleListPop: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var priceListPop: java.util.ArrayList<Int> = java.util.ArrayList<Int>()


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

            val pyear: String = dataSet[position].year //holder로 가져온 값을 변수에 넣기
            val pmonth: String =  dataSet[position].month //holder로 가져온 값을 변수에 넣기
            val pdate: String = dataSet[position].date //holder로 가져온 값을 변수에 넣기

            val pintent: Intent = Intent(holder.itemView?.context, CalendarPopup::class.java) //look_memo.class부분에 원하는 화면 연결

            //변수값 인텐트로 넘기기
            pintent.putExtra("pyear", pyear)
            pintent.putExtra("pmonth", pmonth)
            pintent.putExtra("pdate", pdate)



            // 해당 날짜에 기록된 모든 데이터
            dbDataset=null
            dbDataset =
                (calendardata as HashMap<String, Any>).get(
                    dataSet[position].year+"-"+dataSet[position].month+"-"+dataSet[position].date
                ) as Map<*, *>?

            titleListPop.clear()
            priceListPop.clear()

            if (dbDataset != null && !dbDataset!!.isEmpty()) {
                // 각각의 활동기록 하나씩 반복구문 돌아돌아빙글뱅글어지러워~

                // 혜림아 샹궈 먹고 여기부터 수정해

                for (i in dbDataset!!.values.toTypedArray().indices) {
                    // 해당 날짜에 기록된 첫 번째 데이터
                    val data1 = dbDataset!!.values.toTypedArray()[i]!!
                    val data2: Map<String, Any> = data1 as HashMap<String, Any> //재가공(형변환)

                    // 혜림아 샹궈 먹고 여기부터 수정해
                    titleListPop.add(data2.get("title").toString())
                    priceListPop.add(data2.get("price").toString().toInt())
                }


            }


            pintent.putExtra("titleList", titleListPop)
            pintent.putExtra("priceList", priceListPop)


            Log.d("HEYY pintent", titleListPop.toString())
            Log.d("HEYY pintent", priceListPop.toString())

            ContextCompat.startActivity(holder.itemView?.context, pintent, null) //액티비티 열기



        }





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
                    calendardata = snapshot.getValue() as HashMap<String, Any>?
                    tvDateList.clear() // list 초기화
                    // 키값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    val keys: Set<String> = calendardata!!.keys
                    for (key in keys) {
                        // 활동날짜 리스트에 추가
                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        try {

                            tvDateList.add(key)


                            // 만약 기록이 있는 날이라면
                            for (i:Int in 0 until tvDateList.size){


                                if(dataSet[position].year == tvDateList[i].split("-")[0] &&
                                    dataSet[position].month == tvDateList[i].split("-")[1] &&
                                    dataSet[position].date == tvDateList[i].split("-")[2]){
//                                    holder.outTv.text = "22"
//                                    holder.inTv.text = "0"


                                    // 해당 날짜에 기록된 모든 데이터
                                    dbDataset=null
                                    dbDataset =
                                        (calendardata as HashMap<String, Any>).get(
                                            dataSet[position].year+"-"+dataSet[position].month+"-"+dataSet[position].date
                                        ) as Map<*, *>?

                                    if (dbDataset != null && !dbDataset!!.isEmpty()) {
                                        // 각각의 활동기록 하나씩 반복구문 돌아돌아빙글뱅글어지러워~

                                        // 혜림아 샹궈 먹고 여기부터 수정해
                                        titleList.clear()
                                        priceList.clear()

                                        for (i in dbDataset!!.values.toTypedArray().indices) {
                                            // 해당 날짜에 기록된 첫 번째 데이터
                                            val data1 = dbDataset!!.values.toTypedArray()[i]!!
                                            val data2: Map<String, Any> = data1 as HashMap<String, Any> //재가공(형변환)

                                            // 혜림아 샹궈 먹고 여기부터 수정해
                                            titleList.add(data2.get("title").toString())
                                            priceList.add(data2.get("price").toString().toInt())
                                        }

                                        Log.d("HEYY titleList", titleList.toString())
                                        Log.d("HEYY priceList", priceList.toString())


                                        val dec = DecimalFormat("#,###")
                                        holder.outTv.text = dec.format(priceList.sum()).toString()
                                        holder.inTv.text = ""


                                    }

                                }
                            }

                        } catch (e: Exception) {
                            Log.d("HEY0-walkdateList+222-failed", tvDateList.toString())
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })


        if(dataSet[position].date==""){
            holder.outTv.text = ""
            holder.inTv.text = ""
        }


//        if((dataSet[position].month)==(LocalDate.now().month.toString()).toString()){
//            holder.calRecycleEllipse.visibility = View.VISIBLE
//        } // ** 이거 디베롭해서 오늘 날짜에 초록 표시..
    }

    override fun getItemCount() = dataSet.size
}