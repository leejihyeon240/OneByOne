package com.example.onebyone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onebyone.util.toWon

class SelfAddFinishActivity : AppCompatActivity() {
    private val items by lazy {
        intent.getParcelableArrayListExtra<AddItem>("items")
    }
    private val sales by lazy {
        intent.getParcelableArrayListExtra<AddItem>("sales")
    }
    private val date by lazy {
        intent.getStringExtra("date")
    }

    private val iv_exit by lazy {
        findViewById<ImageView>(R.id.iv_exit)
    }

    private val tvDate by lazy {
        findViewById<TextView>(R.id.tv_date)
    }

    private val rcvResult by lazy {
        findViewById<RecyclerView>(R.id.rcv_result)
    }

    private val tvSale by lazy {
        findViewById<TextView>(R.id.tv_sale)
    }

    private val tvTotal by lazy {
        findViewById<TextView>(R.id.tv_total)
    }

    private var mAdapter: AddFinishRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_add_finish)

        iv_exit.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivityForResult(this, 400)
            }
        }

        tvDate.text = date
        var totalPrice = 0
        var sale = 0

        items?.map {
            totalPrice+=it.price
            it.type = 1
        }

        sales?.forEach{
            sale+=it.price
        }

        mAdapter = AddFinishRecyclerAdapter(items?: arrayListOf(), false)
        rcvResult.adapter = mAdapter

        tvSale.text = "-${sale.toWon()}"
        tvTotal.text = (totalPrice-sale).toWon()
    }
}