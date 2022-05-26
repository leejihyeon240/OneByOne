package com.example.onebyone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var login_login_btn = findViewById<ImageView>(R.id.login_login_btn)
        var login_register_btn = findViewById<ImageView>(R.id.login_register_btn)
        var login_edt_email = findViewById<EditText>(R.id.login_edt_email)
        var login_edit_pwd = findViewById<EditText>(R.id.login_edit_pwd)

        login_login_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        login_register_btn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}