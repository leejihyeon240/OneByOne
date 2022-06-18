package com.example.onebyone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private var mFirebaseAuth: FirebaseAuth? = null
    private lateinit var mDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        //파이어베이스에서 인스턴스 가져오기
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference()

        var login_login_btn = findViewById<ImageView>(R.id.login_login_btn)
        var login_register_btn = findViewById<ImageView>(R.id.login_register_btn)
        var login_edt_email = findViewById<EditText>(R.id.login_edt_email)
        var login_edit_pwd = findViewById<EditText>(R.id.login_edit_pwd)



        login_login_btn.setOnClickListener {

            var home_loading = findViewById(R.id.home_loading) as ImageView
            home_loading.visibility = View.VISIBLE
            Log.d("time1", System.currentTimeMillis().toString())

            mFirebaseAuth!!.signInWithEmailAndPassword(
                login_edt_email.text.toString(),
                login_edit_pwd.text.toString()
            )!!.addOnCompleteListener(this) {
                if (it.isSuccessful) {


                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    Log.d("time3", System.currentTimeMillis().toString())
                    home_loading.visibility = View.GONE

                } else {
                    Toast.makeText(this, "로그인 정보를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }


        login_register_btn.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
            val intent = Intent(this, PythonActivity::class.java)
            startActivity(intent)
        }

    }
}