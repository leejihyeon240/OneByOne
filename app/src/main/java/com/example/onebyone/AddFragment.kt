package com.example.onebyone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.onebyone.listener.DialogListener
import com.example.onebyone.util.toDigit

class AddFragment : DialogFragment() {
    var mDialogListener: DialogListener? = null

    companion object{
        fun getInstance() = AddFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view){
            val etName = findViewById<EditText>(R.id.et_name)
            val etPrice = findViewById<EditText>(R.id.et_price)
            val ivAdd = findViewById<ImageView>(R.id.iv_add)
            // hyerm AddFragment 이거 안쓰이는것같은데 지워도 되려나 일단 문제되는 코드 주석처리함 배고프다
//            ivAdd.setOnClickListener {
//                mDialogListener?.onAdd(AddItem(R.drawable.btn_food_label, title = etName.text.toString(), price = etPrice.text.toString().toDigit()))
//                dismiss()
//            }
        }
    }
}