package com.example.onebyone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.onebyone.listener.ItemClickListener
import kotlinx.android.synthetic.main.activity_camera_add.*
import kotlinx.android.synthetic.main.fragment_daily.*
import kotlinx.android.synthetic.main.fragment_daily.recycler as recycler1

class CameraAddActivity : AppCompatActivity() {
    private val mTvSelectCnt by lazy {
        findViewById<TextView>(R.id.tv_select_cnt)
    }
    private val mCbSelectAll by lazy {
        findViewById<CheckBox>(R.id.cb_select_all)
    }
    private val mIvNext by lazy {
        findViewById<ImageView>(R.id.iv_next)
    }
    private lateinit var mAdapter: AddRecyclerAdapter

    var list = arrayListOf(
        AddItem(R.drawable.btn_food_label, "풀무원국물떡볶이2인", 8900),
        AddItem(R.drawable.btn_food_label, "야채류", 2000),
        AddItem(R.drawable.btn_food_label, "테라갠 500ml*4", 7200),
        AddItem(R.drawable.btn_food_label, "카스캔 355ml*6", 8900),
        AddItem(R.drawable.btn_label_etc_normal, "등 / 초밥의 달인", 3000)
    )

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_add)

        mCbSelectAll.setOnCheckedChangeListener { compoundButton, b ->
            Log.d("adapter-click", "b: $b")
            selectAll(b)
        }

        mAdapter = AddRecyclerAdapter(list)
        with(mAdapter) {
            setListener(object : ItemClickListener {
                override fun onClick(dataList: List<AddItem>, position: Int) {
                    val cnt = dataList.filter { it.isChecked }.size
                    when (cnt) {
                        0 -> {
                            mTvSelectCnt.visibility = View.INVISIBLE
                        }
                        else -> {
                            mTvSelectCnt.visibility = View.VISIBLE
                            mTvSelectCnt.text = "${cnt}개를 선택했습니다!"
                        }
                    }
                }
            })
        }

        with(recycler) {
            adapter = mAdapter
            setHasFixedSize(true)
        }

        mIvNext.setOnClickListener {
            Intent(this, CameraAddActivity2::class.java).apply {
                putParcelableArrayListExtra("items", list.filter { it.isChecked } as ArrayList)
                startActivity(this)
            }
        }
    }

    private fun selectAll(boolean: Boolean) {
        mAdapter.selectAll(boolean)
    }
}