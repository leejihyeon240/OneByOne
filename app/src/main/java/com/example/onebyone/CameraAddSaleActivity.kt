package com.example.onebyone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CameraAddSaleActivity : AppCompatActivity() {
    private val ivClose by lazy {
        findViewById<ImageView>(R.id.iv_close)
    }

    private val ivAddSale by lazy {
        findViewById<ImageView>(R.id.iv_add_sale)
    }

    private val etSaleName by lazy {
        findViewById<EditText>(R.id.et_sale_name)
    }

    private val etSalePrice by lazy {
        findViewById<EditText>(R.id.et_sale_price)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_add_sale)

        ivAddSale.setOnClickListener {
            if(etSaleName.text.toString().isNullOrBlank() || etSalePrice.text.toString().isNullOrBlank()) return@setOnClickListener

            intent.apply {
                putExtra("sale_item", AddItem(R.drawable.btn_food_label, title = etSaleName.text.toString(), price = etSalePrice.text.toString().toInt()))
            }
            setResult(400, intent)
            finish()
        }

        ivClose.setOnClickListener {
            finish()
        }
    }
}