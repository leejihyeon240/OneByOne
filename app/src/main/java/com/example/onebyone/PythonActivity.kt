package com.example.onebyone

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket


class PythonActivity : AppCompatActivity(), View.OnClickListener {
    var connect_btn // ip 받아오는 버튼
            : Button? = null
    var ip_edit // ip 에디트
            : EditText? = null
    var show_text // 서버에서온거 보여주는 에디트
            : TextView? = null

    // hyerm추가 ) 파이썬에서 넘긴 값 저장 ( 0 빼려고 따로 선언)
    var result : Int? = null

    // 소켓통신에 필요한것
    private val html = ""
    private var mHandler: Handler? = null
    private var socket: Socket? = null
    private var dos: DataOutputStream? = null
    private var dis: DataInputStream? = null
    private val ip = "192.168.219.179" // IP 번호
    private val port = 8080 // port 번호
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.onebyone.R.layout.activity_python)
        connect_btn = findViewById<View>(com.example.onebyone.R.id.connect_btn) as Button
        connect_btn!!.setOnClickListener(this)
        ip_edit = findViewById<View>(com.example.onebyone.R.id.ip_edit) as EditText
        show_text = findViewById<View>(com.example.onebyone.R.id.show_text) as TextView
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            com.example.onebyone.R.id.connect_btn -> connect()
        }
    }

    // 로그인 정보 db에 넣어주고 연결시켜야 함.
    fun connect() {
        mHandler = Handler()
        Log.w("connect", "연결 하는중")
        // 받아오는거
        val checkUpdate: Thread = object : Thread() {
            override fun run() {
// ip받기
                val newip = "192.168.0.6"
// 서버 접속
                try {
                    socket = Socket(newip, port)
                    Log.w("서버 접속됨", "서버 접속됨")
                } catch (e1: IOException) {
                    Log.w("서버접속못함", "서버접속못함")
                    e1.printStackTrace()
                }
                Log.w("edit 넘어가야 할 값 : ", "안드로이드에서 서버로 연결요청")
                try {
                    dos = DataOutputStream(socket!!.getOutputStream()) // output에 보낼꺼 넣음
                    dis = DataInputStream(socket!!.getInputStream()) // input에 받을꺼 넣어짐
                    dos!!.writeUTF("항목명") // 이게 파이썬으로 넘어가 지현아 ***
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.w("버퍼", "버퍼생성 잘못됨")
                }
                Log.w("버퍼", "버퍼생성 잘됨")

// 서버에서 계속 받아옴 - 한번은 문자, 한번은 숫자를 읽음. 순서 맞춰줘야 함.
                try {
                    var line = ""
                    var line2: Int
                    while (true) {
                        line = dis!!.readUTF()
                        line2 = dis!!.read()

                        // hyerm 추가
                        if (line2!=0){
                            result=line2
                        }

                        Log.w("서버에서 받아온 값 ", "" + line)
                        Log.w("서버에서 받아온 값 ", "" + line2)
                        Log.w("서버에서 받아온 값 (hyerm 정제) ", "" + result) // 이게 최종 값이야 **
                        show_text?.setText(result.toString())
                    }
                } catch (e: Exception) {
                }
            }
        }
        // 소켓 접속 시도, 버퍼생성
        checkUpdate.start()
    }
}