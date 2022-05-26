package com.example.onebyone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_camera_add2.*

class CameraAddActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_add3)

        cameraAddNextButton.setOnClickListener {
            val intent = Intent(this, CameraAddActivityFinish::class.java)
            startActivity(intent)
        }
    }

}