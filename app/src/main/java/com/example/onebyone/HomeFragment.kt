package com.example.onebyone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    //firebase 1 -----------------------------------/
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null
    /*-----------------------------------------*/


    // private fun updateNum(year: Int, month: Int): Int {
    var pricesum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_home, container, false) as ViewGroup

        var home_loading = rootView.findViewById(R.id.home_loading) as ImageView
        home_loading.visibility = View.VISIBLE
        Log.d("time1", System.currentTimeMillis().toString())


        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne")
            .child("UserAccount")
        /*-----------------------------------------*/


        // 2022년 06월
        var home_status_text1 = rootView.findViewById(R.id.home_status_text1) as TextView
        home_status_text1.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

        // 황오복님 지갑 현황입니다
        // home_inputtext, home_outputtext, home_totaltext
        var home_status_text2 = rootView.findViewById(R.id.home_status_text2) as TextView

        // 이번달 총 지출은 ~ , 저번달 대비 ~
        var home_detailtext1 = rootView.findViewById(R.id.home_detailtext1) as TextView
        var home_detailtext2 = rootView.findViewById(R.id.home_detailtext2) as TextView

        var thisMonth: Int = 0
        var lastMonth: Int = 0


        var dbDataset2: Map<*, *>? = null
        var calendardata2: Map<String, Any>? = null
        var tvDateList2: java.util.ArrayList<String> = java.util.ArrayList<String>()


        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).child("calendar")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    pricesum = 0

                    // calendar에 저장된 모든 데이터
                    calendardata2 = snapshot.getValue() as HashMap<String, Any>?
                    tvDateList2.clear() // list 초기화

                    val keys: Set<String> = calendardata2!!.keys
                    for (key in keys) {
                        tvDateList2.add(key)
                    }

                    try {
                        // 만약 기록이 있는 날이라면
                        for (i: Int in 0 until tvDateList2.size) {

                            if (2022 == tvDateList2[i].split("-")[0].toInt() &&
                                6 == tvDateList2[i].split("-")[1].toInt()
                            ) {

                                // 해당 날짜에 기록된 모든 데이터
                                dbDataset2 = null
                                dbDataset2 =
                                    (calendardata2 as HashMap<String, Any>).get(tvDateList2[i]) as Map<*, *>?

                                for (i in dbDataset2!!.values.toTypedArray().indices) {
                                    // 해당 날짜에 기록된 첫 번째 데이터
                                    val data1 = dbDataset2!!.values.toTypedArray()[i]!!
                                    val data2: Map<String, Any> = data1 as HashMap<String, Any> //재가공(형변환)

                                    pricesum += data2.get("price").toString().toInt()
                                    Log.d("HEY home pricesum", pricesum.toString())
//                                    home_detailtext1.setText(pricesum.toString())

                                    home_detailtext1.setText(NumberFormat.getInstance(Locale.KOREA).format(pricesum)+"원")
                                    thisMonth = pricesum
                                    Log.d("HEY home thisMonth", thisMonth.toString())
                                }
                            }
                        }

                    } catch (e: Exception) {
                        Log.d("HEY cal failed", tvDateList2.toString())
                    }


                }

                override fun onCancelled(error: DatabaseError) {}

            })




        var dbDataset: Map<*, *>? = null
        var calendardata: Map<String, Any>? = null
        var tvDateList: java.util.ArrayList<String> = java.util.ArrayList<String>()


        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).child("calendar")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    pricesum=0

                    // calendar에 저장된 모든 데이터
                    calendardata = snapshot.getValue() as HashMap<String, Any>?
                    tvDateList.clear() // list 초기화

                    val keys: Set<String> = calendardata!!.keys
                    for (key in keys) {
                        tvDateList.add(key)
                    }

                    try {
                        // 만약 기록이 있는 날이라면
                        for (i: Int in 0 until tvDateList.size) {

                            if (2022 == tvDateList[i].split("-")[0].toInt() &&
                                5 == tvDateList[i].split("-")[1].toInt()
                            ) {

                                // 해당 날짜에 기록된 모든 데이터
                                dbDataset = null
                                dbDataset =
                                    (calendardata as HashMap<String, Any>).get(tvDateList[i]) as Map<*, *>?

                                for (i in dbDataset!!.values.toTypedArray().indices) {
                                    // 해당 날짜에 기록된 첫 번째 데이터
                                    val data1 = dbDataset!!.values.toTypedArray()[i]!!
                                    val data2: Map<String, Any> = data1 as HashMap<String, Any> //재가공(형변환)

                                    pricesum += data2.get("price").toString().toInt()
                                    Log.d("HEY home pricesum", pricesum.toString())
//                                    home_detailtext2.setText(pricesum.toString())

                                    if(thisMonth-pricesum>0){

                                        home_detailtext2.setText(
                                            NumberFormat.getInstance(Locale.KOREA).format((thisMonth-pricesum))+"원 더 소비")

                                    }
                                    else{
                                        home_detailtext2.setText(
                                            NumberFormat.getInstance(Locale.KOREA).format((thisMonth-pricesum))+"원 덜 소비")
                                    }

                                    lastMonth=pricesum
                                    Log.d("HEY home thisMonth", thisMonth.toString())
                                }
                            }
                        }

                    } catch (e: Exception) {
                        Log.d("HEY cal failed", tvDateList.toString())
                    }



                }

                override fun onCancelled(error: DatabaseError) {}

            })


//        home_detailtext1.setText("이번달 총 지출은 "+
//                NumberFormat.getInstance(Locale.KOREA).format(pricesum)+"원 이군요")
        Log.d("HEY home thisMonth", thisMonth.toString())
        Log.d("HEY home lastMonth", lastMonth.toString())




        var home_box_camera = rootView.findViewById(R.id.home_box_camera) as ImageView
        var home_box_self = rootView.findViewById(R.id.home_box_self) as ImageView

        home_box_camera.setOnClickListener {
            var intent: Intent = Intent(context, CameraActivity::class.java)
            startActivity(intent)
        }

        home_box_self.setOnClickListener {
            var intent: Intent = Intent(context, SelfAddActivity::class.java)
            startActivity(intent)
        }

        home_loading.visibility = View.GONE

        Log.d("time3", System.currentTimeMillis().toString())
        return rootView

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun updateNum(year: Int, month: Int): Int {

        var dbDataset: Map<*, *>? = null
        var calendardata: Map<String, Any>? = null
        var tvDateList: java.util.ArrayList<String> = java.util.ArrayList<String>()


        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).child("calendar")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    pricesum = 0

                    // calendar에 저장된 모든 데이터
                    calendardata = snapshot.getValue() as HashMap<String, Any>?
                    tvDateList.clear() // list 초기화

                    val keys: Set<String> = calendardata!!.keys
                    for (key in keys) {
                        tvDateList.add(key)
                    }
                    Log.d("HEY home tvDateList", tvDateList.toString())
                    Log.d("HEY home tvDateList[0] year", tvDateList[0].split("-")[0])
                    Log.d("HEY home tvDateList[0] month", tvDateList[0].split("-")[1])
                    Log.d("HEY home year", year.toString())
                    Log.d("HEY home month", month.toString())


                }

                override fun onCancelled(error: DatabaseError) {}

            })

        try {
            // 만약 기록이 있는 날이라면
            for (i: Int in 0 until tvDateList.size) {

                if (year == tvDateList[i].split("-")[0].toInt() &&
                    month == tvDateList[i].split("-")[1].toInt()
                ) {


                    Log.d("HEY home tvDateList[i] year", tvDateList[i].split("-")[0])
                    Log.d("HEY home tvDateList[i] month", tvDateList[i].split("-")[1])

                    // 해당 날짜에 기록된 모든 데이터
                    dbDataset = null
                    dbDataset =
                        (calendardata as HashMap<String, Any>).get(tvDateList[i]) as Map<*, *>?

                    for (i in dbDataset!!.values.toTypedArray().indices) {
                        // 해당 날짜에 기록된 첫 번째 데이터
                        val data1 = dbDataset!!.values.toTypedArray()[i]!!
                        val data2: Map<String, Any> = data1 as HashMap<String, Any> //재가공(형변환)

                        pricesum += data2.get("price").toString().toInt()
                        Log.d("HEY home pricesum", pricesum.toString())
                    }
                }
            }

        } catch (e: Exception) {
            Log.d("HEY cal failed", tvDateList.toString())
        }

        Log.d("HEY home pricesum3", pricesum.toString())

        Log.d("HEY home pricesum return !!", pricesum.toString())


        return pricesum
    }
}