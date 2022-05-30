package com.example.onebyone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_camera_add3.*
import java.util.*

class SelfAddActivity2 : AppCompatActivity() {
    private val mAddSaleRequestCode = 400

    private val ivBack by lazy {
        findViewById<ImageView>(R.id.iv_back)
    }

    private val items by lazy {
        intent?.getParcelableArrayListExtra<AddItem>("items")
    }

    private val rcvSale by lazy {
        findViewById<RecyclerView>(R.id.rcv_sale)
    }

    private val ivNext by lazy {
        findViewById<ImageView>(R.id.iv_next)
    }

    private val etYear by lazy {
        findViewById<EditText>(R.id.et_year)
    }

    private val etMonth by lazy {
        findViewById<EditText>(R.id.et_month)
    }

    private val etDay by lazy {
        findViewById<EditText>(R.id.et_day)
    }

    private var mAdapter: Add3RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_add2)

        ivBack.setOnClickListener {
            finish()
        }

        Log.d("bundle", items.toString())

        mAdapter = Add3RecyclerAdapter(arrayListOf(), true)
        rcvSale.adapter = mAdapter


        cameraadd3_plus.setOnClickListener {
            Intent(this, CameraAddSaleActivity::class.java).apply {
                startActivityForResult(this, 400)
            }
        }

        ivNext.setOnClickListener {
            Intent(this, SelfAddFinishActivity::class.java).apply {
                putParcelableArrayListExtra("items", items)
                putParcelableArrayListExtra("sales", mAdapter!!.getList())
                putExtra("date", "${etYear.text.toString()}, ${etMonth.text.toString()}, ${etDay.text.toString()}")
                startActivity(this)
                finish()
            }
        }

        val date = Date()
        etYear.setText("${date.year + 1900}")
        etMonth.setText("${date.month + 1}")
        etDay.setText("${date.date}")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            mAddSaleRequestCode -> {
                val item = data?.getParcelableExtra<AddItem>("sale_item")
                Log.d("sale", item.toString())

                item?.let {
                    mAdapter!!.addItem(it)
                }
            }
        }
    }
}