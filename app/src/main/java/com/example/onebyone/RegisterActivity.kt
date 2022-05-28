package com.example.onebyone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class RegisterActivity : AppCompatActivity() {


    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFirebaseAuth = FirebaseAuth.getInstance()


        var register_go_to_login_btn = findViewById<ImageView>(R.id.register_go_to_login_btn)
        var register_finish_btn = findViewById<ImageView>(R.id.register_finish_btn)

        var register_edt_name = findViewById<EditText>(R.id.register_edt_name)
        var register_edt_email = findViewById<EditText>(R.id.register_edt_email)
        var register_edt_pwd = findViewById<EditText>(R.id.register_edt_pwd)
        var register_edt_pwd_chk = findViewById<EditText>(R.id.register_edt_pwd_chk)
        var tvEmailChk = findViewById<TextView>(R.id.tvEmailChk)

        register_finish_btn.isEnabled = false

        tvEmailChk.setOnClickListener(View.OnClickListener {
            if (!register_edt_email.getText().toString().isEmpty()) { //이메일 칸이 비어있지 않으면
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne").child("UserAccount")
                mDatabaseRef!!.orderByChild("email").equalTo(register_edt_email.getText().toString())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            Log.d("TAG", register_edt_email.getText().toString())
                            if (!snapshot.exists()) {
                                Toast.makeText(this@RegisterActivity, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show()
                                register_finish_btn.isEnabled = true
                            } else {
                                Toast.makeText(this@RegisterActivity, "이미 가입되어 있는 아이디입니다.", Toast.LENGTH_SHORT).show()
                                register_finish_btn.isEnabled = false
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            } else {
                Toast.makeText(this@RegisterActivity, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        })





        register_finish_btn.setOnClickListener {

            var user_email : String = register_edt_email.text.toString()
            var user_pwd : String = register_edt_pwd.text.toString()
            var user_name : String = register_edt_name.text.toString()

            //모든 항목을 작성해야 회원가입 가능하게 함
            if(user_name.equals("") || user_pwd.equals("") || user_name.equals("")){
                Toast.makeText(this, "가입 정보를 모두 입력하세요", Toast.LENGTH_SHORT).show()
            }else{
                //파이어베이스에 유저를 등록하고, 리얼타임 데이터베이스에 사용자 정보들을 setValue로 넣어줌
                mFirebaseAuth!!.createUserWithEmailAndPassword(user_email, user_pwd)?.addOnCompleteListener(this){
                    if(it.isSuccessful){
                        val mFirebaseUser : FirebaseUser? = mFirebaseAuth?.currentUser
                        val user_uid :String = mFirebaseUser!!.uid
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne").child("UserAccount").child(user_uid)

                        val hashMap : HashMap<String, String> = HashMap()
                        hashMap.put("uid", user_uid)
                        hashMap.put("name", user_name)
                        hashMap.put("email", user_email)
                        hashMap.put("pwd", user_pwd)


                        mDatabaseRef!!.setValue(hashMap)

                        Toast.makeText(this, "$user_name 님, 가입을 축하합니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, RegisterFinishActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "입력 정보를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        register_go_to_login_btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}