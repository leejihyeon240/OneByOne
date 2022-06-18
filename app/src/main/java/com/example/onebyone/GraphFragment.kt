package com.example.onebyone

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class GraphFragment : Fragment() {
    // TODO: Rename and change types of parameters


    //firebase 1 -----------------------------------/
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null
    /*-----------------------------------------*/

    var dbDataset: Map<*, *>? = null
    var graph_view_date = LocalDate.now()
    var graphdata: Map<String, Any>? = null
    var tvDateList: java.util.ArrayList<String> = java.util.ArrayList<String>()

    var a : Float? = null

    val entries1 = ArrayList<PieEntry>()
    val colorsItems1 = ArrayList<Int>()

    var graph_text1 : TextView? = null
    var graph_text2 : TextView? = null
    var graph_text3 : TextView? = null
    var graph_text4 : TextView? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment\


        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne")
            .child("UserAccount")
        /*-----------------------------------------*/


        val view = inflater.inflate(R.layout.fragment_graph, container, false)

        val graph_titletext: TextView = view.findViewById(R.id.graph_titletext)


        graph_titletext.setText(graph_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())



        var graph_loading = view.findViewById(R.id.graph_loading) as ImageView
        graph_loading.visibility=View.VISIBLE
        Log.d("time1",System.currentTimeMillis().toString())



        val anim_toright = AnimationUtils.loadAnimation(activity, R.anim.graph_toright)
        val anim_toleft = AnimationUtils.loadAnimation(activity, R.anim.graph_toleft)

        var graphMainbox1: RelativeLayout = view.findViewById(R.id.graph_mainbox1)


        var chart1: PieChart = view.findViewById(R.id.graph_piechart1) as PieChart


        graph_text1 = view.findViewById(R.id.graph_text1) //ㅇㅇㅇ님은
        graph_text2 = view.findViewById(R.id.graph_text2) //"땡떙땡님은 ㅇㅇ로 가장 많은" 여기서 ㅇㅇ 부분
        graph_text3 = view.findViewById(R.id.graph_text3) //(으)로 가장 많은 지출이 있었네요
        graph_text4 = view.findViewById(R.id.graph_text4) //아직 아무것도 등록되지 않았어요ㅠ

        graph_text2!!.setPaintFlags(graph_text2!!.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)


//        chart.setUsePercentValues(true)
        chart1.getDescription().setEnabled(false);
//        chart.setDragDecelerationFrictionCoef(0.95f);
        chart1.setDrawHoleEnabled(false);
        chart1.setHoleColor(Color.WHITE);
        chart1.setTransparentCircleRadius(61f);



        //그래프 month 왼쪽 버튼 클릭
        var graph_leftbutton = view.findViewById(R.id.graph_leftbutton) as ImageView
        graph_leftbutton.setOnClickListener {
            graph_view_date = graph_view_date.minusMonths(1)
            graph_titletext.setText(graph_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

            mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var gmonth : String = graph_view_date.format(DateTimeFormatter.ofPattern("M")).toString()

                        val map = HashMap<String, Any>()
                        map["gmonth"] = gmonth
                        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).updateChildren(map)

                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

        }


        //그래프 month 오른쪽 버튼 클릭
        var graph_rightbutton = view.findViewById(R.id.graph_rightbutton) as ImageView
        graph_rightbutton.setOnClickListener {
            graph_view_date = graph_view_date.plusMonths(1)
            graph_titletext.setText(graph_view_date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

            mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var gmonth : String = graph_view_date.format(DateTimeFormatter.ofPattern("M")).toString()

                        val map = HashMap<String, Any>()
                        map["gmonth"] = gmonth
                        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).updateChildren(map)

                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

        }

        //달이 바뀔 떄마다 그래프 변화
        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val user = snapshot.getValue(UserAccount::class.java)

                    graph_text1!!.visibility = View.VISIBLE
                    graph_text2!!.visibility = View.VISIBLE
                    graph_text3!!.visibility = View.VISIBLE
                    graph_text4!!.visibility = View.INVISIBLE
                    graph_text1!!.setText(user!!.getName().toString() + "님은")


                    var tempMap = mutableMapOf<String, Int>()
                    entries1.clear()

                    mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid).child("calendar")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                var typeList : ArrayList<String>? = null

                                // calendar에 저장된 모든 데이터
                                graphdata = snapshot.getValue() as java.util.HashMap<String, Any>?
                                tvDateList.clear() // list 초기화

                                // 키값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                val keys: Set<String> = graphdata!!.keys
                                for (key in keys) {
                                    tvDateList.add(key)
                                }

                                try{
                                    for(i: Int in 0 until tvDateList.size){
                                        if (graph_view_date.year == tvDateList[i].split("-")[0].toInt() &&
                                            graph_view_date.monthValue == tvDateList[i].split("-")[1].toInt()){

                                            // 해당 날짜에 기록된 모든 데이터
                                            dbDataset = null
                                            dbDataset =
                                                (graphdata as java.util.HashMap<String, Any>).get(tvDateList[i]) as Map<*, *>?

                                            Log.d("HEY cal dbDataset", dbDataset.toString())

                                            for (i in dbDataset!!.values.toTypedArray().indices) {
                                                // 해당 날짜에 기록된 첫 번째 데이터
                                                val data1 = dbDataset!!.values.toTypedArray()[i]!!
                                                val data2: Map<String, Any> = data1 as java.util.HashMap<String, Any> //재가공(형변환)

                                                var type : String = data2.get("type").toString()
                                                var price : Int = data2.get("price").toString().toInt()


                                                if(tempMap.containsKey(type)){
                                                    tempMap[type] = tempMap.get(type).toString().toInt() + price
                                                }else{
                                                    tempMap.put(type, price)
                                                }

/*                                                map2.forEach { key, value ->
                                                    map1.merge(
                                                        key,
                                                        value
                                                    ) { v1, v2 -> v1 + v2 }
                                                }*/



                                                Log.d("YJ graph type tempMap", tempMap.toString())
                                                //typeList!!.add(data2.get("type").toString())

                                                Log.d("YJ graph typeList", data1.toString())
//                                    priceList.add(data2.get("price").toString().toInt())

                                            }
                                        }


                                    }

                                    tempMap.remove("할인")

                                    var sortedByValue = tempMap.toList().sortedWith(compareBy({it.second})).toMap()


                                    sortedByValue.forEach { key, value ->

                                        entries1.add(PieEntry(value.toFloat(), key))
                                        graph_text2!!.setText(key)
                                    }


                                    Log.d("YJ graph type sortedByDescending", sortedByValue.toString())


                                    a = tempMap.get("식품").toString().toFloat()
                                    Log.d("YJ graph type a", a.toString())

                                    /*----------그래프 실행 시작--------*/


                                    // add a lot of colors - 수입
                                    colorsItems1.add(Color.parseColor("#A4D3B7"))
                                    colorsItems1.add(Color.parseColor("#6AB98A"))
                                    colorsItems1.add(Color.parseColor("#41A76A"))
                                    colorsItems1.add(Color.parseColor("#309F5D"))


                                    // dataset 정의 - 수입
                                    val pieDataSet1 = PieDataSet(entries1, "")
                                    pieDataSet1.apply {
                                        colors = colorsItems1
                                        valueTextColor = Color.parseColor("#F2F6F3")
                                        valueTextSize = 16f
                                    }
                                    pieDataSet1.setDrawValues(false)
                                    val pieDataResult1 = PieData(pieDataSet1)

                                    chart1.run {
                                        data = pieDataResult1
                                        description.isEnabled = false
                                        isRotationEnabled = false
//            centerText = "This is Center"
                                        setEntryLabelColor(Color.WHITE)
//            animateY(1400, Easing.EaseInOutCubic)
                                        animateY(300, Easing.EaseInOutCubic)
                                        animate()
                                    }
                                    /*----------그래프 실행 끝--------*/


                                }catch (e: Exception){
                                    Log.d("YJ graph fail", tvDateList.toString())

                                    graph_text1!!.visibility = View.INVISIBLE
                                    graph_text2!!.visibility = View.INVISIBLE
                                    graph_text3!!.visibility = View.INVISIBLE
                                    graph_text4!!.visibility = View.VISIBLE
                                    /*----------그래프 실행 시작--------*/
                                    // data - 월별
                                    val entries1 = ArrayList<PieEntry>()
                                    entries1.add(PieEntry(14000f, "아직 데이터 없어용"))

                                    // add a lot of colors - 수입
                                    val colorsItems1 = ArrayList<Int>()
                                    colorsItems1.add(Color.parseColor("#309F5D"))


                                    // dataset 정의 - 수입
                                    val pieDataSet1 = PieDataSet(entries1, "")
                                    pieDataSet1.apply {
                                        colors = colorsItems1
                                        valueTextColor = Color.parseColor("#F2F6F3")
                                        valueTextSize = 16f
                                    }
                                    pieDataSet1.setDrawValues(false)
                                    val pieDataResult1 = PieData(pieDataSet1)

                                    chart1.run {
                                        data = pieDataResult1
                                        description.isEnabled = false
                                        isRotationEnabled = false
//            centerText = "This is Center"
                                        setEntryLabelColor(Color.WHITE)
//            animateY(1400, Easing.EaseInOutCubic)
                                        animateY(300, Easing.EaseInOutCubic)
                                        animate()
                                    }
                                    /*----------그래프 실행 끝--------*/
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })

                }

                override fun onCancelled(error: DatabaseError) {}
            })


//        chart.invalidate()

        graph_loading.visibility=View.GONE


        return view
//        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

}