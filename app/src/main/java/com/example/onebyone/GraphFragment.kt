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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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

        var graph_loading = view.findViewById(R.id.graph_loading) as ImageView
        graph_loading.visibility=View.VISIBLE
        Log.d("time1",System.currentTimeMillis().toString())

        var graph_leftbutton = view.findViewById(R.id.graph_leftbutton) as ImageView
        graph_leftbutton.setOnClickListener {
            Log.d("graph click test", "dd")
        }


        val anim_toright = AnimationUtils.loadAnimation(activity, R.anim.graph_toright)
        val anim_toleft = AnimationUtils.loadAnimation(activity, R.anim.graph_toleft)

        var graphMainbox1: RelativeLayout = view.findViewById(R.id.graph_mainbox1)


        var chart1: PieChart = view.findViewById(R.id.graph_piechart1) as PieChart


        var graphText2: TextView = view.findViewById(R.id.graph_text2)
        var graphText3: TextView = view.findViewById(R.id.graph_text3)
        graphText2.setPaintFlags(graphText2.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)


//        chart.setUsePercentValues(true)
        chart1.getDescription().setEnabled(false);
//        chart.setDragDecelerationFrictionCoef(0.95f);
        chart1.setDrawHoleEnabled(false);
        chart1.setHoleColor(Color.WHITE);
        chart1.setTransparentCircleRadius(61f);



        // data - 월별
        val entries1 = ArrayList<PieEntry>()
        entries1.add(PieEntry(85f, "월급"))
        entries1.add(PieEntry(10f, "이자"))
        entries1.add(PieEntry(5f, "기타"))

        // add a lot of colors - 수입
        val colorsItems1 = ArrayList<Int>()
        colorsItems1.add(Color.parseColor("#309F5D"))
        colorsItems1.add(Color.parseColor("#41A76A"))
        colorsItems1.add(Color.parseColor("#6AB98A"))
        colorsItems1.add(Color.parseColor("#A4D3B7"))


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
//        chart.invalidate()

        graph_loading.visibility=View.GONE
        return view
//        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

}