package com.example.onebyone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_camera_add3.*
import java.util.Date

class CameraAddActivity3 : AppCompatActivity() {

    private var mFirebaseAuth : FirebaseAuth? = null
    private lateinit var mDatabaseRef : DatabaseReference

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
        setContentView(R.layout.activity_camera_add3)

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
            Intent(this, CameraAddActivityFinish::class.java).apply {
                putParcelableArrayListExtra("items", items)
                putParcelableArrayListExtra("sales", mAdapter!!.getList())
                putExtra("date", "${etYear.text.toString()}-${etMonth.text.toString()}-${etDay.text.toString()}")



                //firebase

                //firebase
                mFirebaseAuth = FirebaseAuth.getInstance()
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne")
                    .child("UserAccount")

//                val map = HashMap<String, Any>()
//
//                map["addItem"] = items.toString()
//
//                mDatabaseRef.child(mFirebaseAuth!!.currentUser!!.uid).
//                child("calendar").child("${etYear.text.toString()}-${etMonth.text.toString()}-${etDay.text.toString()}").push().setValue(map)


                Log.d("dbhyerm title", items?.get(0)!!.getTitleHyerm() )
                Log.d("dbhyerm price", items?.get(0)!!.getPriceHyerm().toString() )

                for (i:Int in 0 .. items!!.size-1) {
                    Log.d("dbhyerm title", items?.get(i)!!.getTitleHyerm() )
                    Log.d("dbhyerm price", items?.get(i)!!.getPriceHyerm().toString() )
                    Log.d("dbhyerm type", items?.get(i)!!.getTypeHyerm().toString() )

                    val map = HashMap<String, Any>()

                    map.put("title",items?.get(i)!!.getTitleHyerm())
                    map.put("price",items?.get(i)!!.getPriceHyerm())
                    map.put("type",items?.get(i)!!.getTypeHyerm())

                    mDatabaseRef.child(mFirebaseAuth!!.currentUser!!.uid).
                    child("calendar").child("${etYear.text.toString()}-${etMonth.text.toString()}-${etDay.text.toString()}").push().setValue(map)

                }

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
                    mAdapter!!.addItem(item)
                }
            }
        }
    }
}