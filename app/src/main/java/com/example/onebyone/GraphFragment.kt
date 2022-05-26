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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class GraphFragment : Fragment() {
    // TODO: Rename and change types of parameters

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment\
        val view = inflater.inflate(R.layout.fragment_graph, container, false)

        var graph_leftbutton = view.findViewById(R.id.graph_leftbutton) as ImageView
        graph_leftbutton.setOnClickListener {
            Log.d("graph click test", "dd")
        }

        var graphInbutton: LinearLayout = view.findViewById(R.id.graph_inbutton)
        var graphOutbutton: LinearLayout = view.findViewById(R.id.graph_outbutton)
        var graphInText: TextView = view.findViewById(R.id.graph_intext)
        var graphOutText: TextView = view.findViewById(R.id.graph_outtext)
        var graphSelectBox = view.findViewById(R.id.graph_selectbox) as ImageView
        var isLeft = true

        val anim_toright = AnimationUtils.loadAnimation(activity, R.anim.graph_toright)
        val anim_toleft = AnimationUtils.loadAnimation(activity, R.anim.graph_toleft)

        var graphMainbox1: RelativeLayout = view.findViewById(R.id.graph_mainbox1)
        var graphMainbox2: RelativeLayout = view.findViewById(R.id.graph_mainbox2)
        graphMainbox1.visibility = View.VISIBLE
        graphMainbox2.visibility = View.INVISIBLE


        var chart1: PieChart = view.findViewById(R.id.graph_piechart1) as PieChart
        var chart2: PieChart = view.findViewById(R.id.graph_piechart2) as PieChart


        var graphText2: TextView = view.findViewById(R.id.graph_text2)
        var graphText3: TextView = view.findViewById(R.id.graph_text3)
        graphText2.setPaintFlags(graphText2.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)


//        chart.setUsePercentValues(true)
        chart1.getDescription().setEnabled(false);
//        chart.setDragDecelerationFrictionCoef(0.95f);
        chart1.setDrawHoleEnabled(false);
        chart1.setHoleColor(Color.WHITE);
        chart1.setTransparentCircleRadius(61f);


        chart2.getDescription().setEnabled(false);
        chart2.setDrawHoleEnabled(false);
        chart2.setHoleColor(Color.WHITE);
        chart2.setTransparentCircleRadius(61f);

        // data - 수입
        val entries1 = ArrayList<PieEntry>()
        entries1.add(PieEntry(85f, "월급"))
        entries1.add(PieEntry(10f, "이자"))
        entries1.add(PieEntry(5f, "기타"))

        // data - 지출
        val entries2 = ArrayList<PieEntry>()
        entries2.add(PieEntry(41f, "식사"))
        entries2.add(PieEntry(29f, "카페"))
        entries2.add(PieEntry(12f, "선물"))
        entries2.add(PieEntry(9f, "교통비"))

        // add a lot of colors - 수입
        val colorsItems1 = ArrayList<Int>()
        colorsItems1.add(Color.parseColor("#309F5D"))
        colorsItems1.add(Color.parseColor("#41A76A"))
        colorsItems1.add(Color.parseColor("#6AB98A"))
        colorsItems1.add(Color.parseColor("#A4D3B7"))

        // add a lot of colors - 지출
        val colorsItems2 = ArrayList<Int>()
        colorsItems2.add(Color.parseColor("#DD6043"))
        colorsItems2.add(Color.parseColor("#E17157"))
        colorsItems2.add(Color.parseColor("#E78B76"))
        colorsItems2.add(Color.parseColor("#F0B6A8"))

        // dataset 정의 - 수입
        val pieDataSet1 = PieDataSet(entries1, "")
        pieDataSet1.apply {
            colors = colorsItems1
            valueTextColor = Color.parseColor("#F2F6F3")
            valueTextSize = 16f
        }
        pieDataSet1.setDrawValues(false)
        val pieDataResult1 = PieData(pieDataSet1)

        // dataset 정의 - 지출
        val pieDataSet2 = PieDataSet(entries2, "")
        pieDataSet2.apply {
            colors = colorsItems2
            valueTextColor = Color.parseColor("#DD6043")
            valueTextSize = 16f
        }
        pieDataSet2.setDrawValues(false)
        val pieDataResult2 = PieData(pieDataSet2)

//        chart.setData(pieDataResult)

        chart1.run {
            data = pieDataResult1
            description.isEnabled = false
            isRotationEnabled = false
//            centerText = "This is Center"
            setEntryLabelColor(Color.WHITE)
//            animateY(1400, Easing.EaseInOutCubic)
            animateY(700)
            animate()
        }
//        chart.invalidate()

        chart2.run {
            data = pieDataResult2
            description.isEnabled = false
            isRotationEnabled = false
//            centerText = "This is Center"
            setEntryLabelColor(Color.WHITE)
//            animateY(1400, Easing.EaseInOutCubic)
//            animateY(700)
//            animate()
        }

        // 수입 버튼 터치
        graphInbutton.setOnClickListener {
            if (!isLeft) {
                Log.d("graph", "right -> left")
                graphSelectBox.startAnimation(anim_toleft) // 애니메이션 재생
                graphInText.setTextColor(Color.parseColor("#3A4935")) // 수입 : 진하게
                graphOutText.setTextColor(Color.parseColor("#929893")) // 지출 : 연하게
                isLeft = !isLeft
                graphMainbox1.visibility = View.VISIBLE
                graphMainbox2.visibility = View.INVISIBLE
                graphText2.setText("월급")
                graphText2.setTextColor(Color.parseColor("#309F5D"))
                graphText2.setPaintFlags(graphText2.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                graphText3.setText("으로 가장 많은 수입이 있었네요!")
//                chart.setData(pieDataResult1)
//                chart.invalidate()
//                chart.animate()
            }
            Log.d("graph", "left -> left")
        }

        // 지출 버튼 터치
        graphOutbutton.setOnClickListener {
            if (isLeft) {
                Log.d("graph", "left -> right")
                graphSelectBox.startAnimation(anim_toright) // 애니메이션 재생
                graphInText.setTextColor(Color.parseColor("#929893")) // 수입 : 연하게
                graphOutText.setTextColor(Color.parseColor("#3A4935")) // 지출 : 진하게
                isLeft = !isLeft
                graphMainbox1.visibility = View.INVISIBLE
                graphMainbox2.visibility = View.VISIBLE
                graphText2.setText("식사")
                graphText2.setTextColor(Color.parseColor("#DD6043"))
                graphText2.setPaintFlags(graphText2.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                graphText3.setText("에 가장 많은 소비를 했어요")
//                chart.setData(pieDataResult2)
//                chart.invalidate()
//                chart.animate()
            }
            Log.d("graph", "right -> right")
        }

        return view
//        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

}