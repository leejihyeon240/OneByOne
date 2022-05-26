package com.example.onebyone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        var register_go_to_login_btn = findViewById<ImageView>(R.id.register_go_to_login_btn)
        var register_finish_btn_grey = findViewById<ImageView>(R.id.register_finish_btn_grey)
        var register_finish_btn_green = findViewById<ImageView>(R.id.register_finish_btn_green)

        var register_edt_name = findViewById<EditText>(R.id.register_edt_name)
        var register_edt_email = findViewById<EditText>(R.id.register_edt_email)
        var register_edt_pwd = findViewById<EditText>(R.id.register_edt_pwd)
        var register_edt_pwd_chk = findViewById<EditText>(R.id.register_edt_pwd_chk)

        register_finish_btn_green.setOnClickListener {
            val intent = Intent(this, RegisterFinishActivity::class.java)
            startActivity(intent)
            finish()
        }

        register_go_to_login_btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}