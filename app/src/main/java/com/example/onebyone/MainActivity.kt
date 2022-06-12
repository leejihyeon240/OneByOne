package com.example.onebyone

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 하단 탭이 눌렸을 때 화면을 전환하기 위해선 이벤트 처리하기 위해 BottomNavigationView 객체 생성
        var main_menu = findViewById(R.id.main_menu) as BottomNavigationView

        // topbar 아이콘, 텍스트뷰
        var main_topbar_icon = findViewById<ImageView>(R.id.main_topbar_icon)
        var main_topbar_text = findViewById<TextView>(R.id.main_topbar_text)

        var intent : Intent = getIntent()
        var year : Int = intent.getIntExtra("daily_pyear", 0)
        var month : Int = intent.getIntExtra("daily_pmonth", 0)
        var date : Int = intent.getIntExtra("daily_pdate", 0)
        Log.d("HEY main activity - year", year.toString())
        Log.d("HEY main activity - month", month.toString())
        Log.d("HEY main activity - date", date.toString())

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        main_menu.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.homeButton -> {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    main_topbar_icon.visibility= View.VISIBLE
                    main_topbar_text.visibility= View.INVISIBLE
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, homeFragment).commit()
                }
                R.id.calendarButton -> {
                    main_topbar_icon.visibility= View.INVISIBLE
                    main_topbar_text.setText("캘린더")
                    main_topbar_text.visibility= View.VISIBLE
                    val calendarFragment = CalendarFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, calendarFragment).commit()
                }
                R.id.dailyButton -> {
                    main_topbar_icon.visibility= View.INVISIBLE
                    main_topbar_text.setText("일별")
                    main_topbar_text.visibility= View.VISIBLE
                    val dailyFragment = DailyFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, dailyFragment).commit()
                }
                R.id.graphButton -> {
                    main_topbar_icon.visibility= View.INVISIBLE
                    main_topbar_text.setText("통계")
                    main_topbar_text.visibility= View.VISIBLE
                    val graphFragment = GraphFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, graphFragment).commit()
                }
            }
            true
        }
            selectedItemId = R.id.homeButton
        }


    }
}