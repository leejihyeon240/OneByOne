package com.example.onebyone

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_camera_add.*

class CameraAddActivity : AppCompatActivity() {

    var list = arrayListOf<AddItem>(
        AddItem(R.drawable.cameraadd_label_button, "플무원국물떡볶이2인", "8,900"),
        AddItem(R.drawable.cameraadd_label_button, "야채류", "2,000"),
        AddItem(R.drawable.cameraadd_label_button, "테라갠 500ml*4", "7,200"),
        AddItem(R.drawable.cameraadd_label_button, "카스캔 355ml*6", "8,900"),
        AddItem(R.drawable.cameraadd_label_etc_button, "등 / 초밥의 달인", "3,000")

    )

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_add)

        val mAdapter = AddRecyclerAdapter(this, list)

        mAdapter.setItemClickListener(object: AddRecyclerAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

            }
        })

        recycler.adapter = mAdapter

        val layout = LinearLayoutManager(this)
        recycler.layoutManager = layout
        recycler.setHasFixedSize(true)

        cameraAddNextButton.setOnClickListener {
            val intent = Intent(this, CameraAddActivity2::class.java)
            startActivity(intent)
        }
    }
}