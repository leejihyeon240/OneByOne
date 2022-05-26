package com.example.onebyone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_camera_add.*
import kotlinx.android.synthetic.main.activity_camera_add_revise.*

class CameraAddReviseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_add_revise)

        add_clear_button.setOnClickListener {
            finish()
        }
    }
}