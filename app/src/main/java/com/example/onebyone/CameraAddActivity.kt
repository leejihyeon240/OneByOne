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


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_add)

        var intent : Intent = getIntent()

        var list_category = intent.getSerializableExtra("LIST_CATEGORY") as ArrayList<String>
        var list_name = intent.getSerializableExtra("LIST_NAME") as ArrayList<String>
        var list_price = intent.getSerializableExtra("LIST_PRICE") as ArrayList<Int>

        Log.d("yeonji-result", list_name.toString())
        Log.d("yeonji-result", list_price.toString())

        var list = arrayListOf<AddItem>()

        for(i in 0 until list_name.size step(1)){

            Log.d("yeonji-a",list_name.size.toString())

            // 지현아 여기 >R.drawable.cb_add_food< 수정해야 해 파이썬 연동하고! (일단 디폴트값으로 푸트 넣은거야)
            list.add(AddItem(R.drawable.cb_add_food,list_name[i], list_price[i], list_category[i]))


            Log.d("yeonji-b",list.toString())
            Log.d("yjyjyjyj",list.size.toString())
        }

        mCbSelectAll.setOnCheckedChangeListener { compoundButton, b ->
            Log.d("adapter-click", "b: $b")
            selectAll(b)
        }

        mAdapter = AddRecyclerAdapter(list!!)
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