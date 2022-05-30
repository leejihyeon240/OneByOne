package com.example.onebyone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.onebyone.listener.DialogListener

class ReviseFragment : DialogFragment() {
    private lateinit var mItem: AddItem
    private var mPosition = -1

    var mDialogListener: DialogListener? = null

    companion object {
        fun getInstance() = ReviseFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_revise, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            mItem = it.getParcelable("item")!!
            mPosition = it.getInt("position", -1)
        }

        with(view) {
            val etName = findViewById<EditText>(R.id.et_name)
            val etPrice = findViewById<EditText>(R.id.et_price)
            val ivRevise = findViewById<ImageView>(R.id.iv_revise)
            val ivDelete = findViewById<ImageView>(R.id.iv_delete)

            mItem.let {
                etName.setText(it.title)
                etPrice.setText(it.price.toString())
            }

            ivRevise.setOnClickListener {
                val mPrice = try {
                    etPrice.text.toString().replace(",", "").toInt()
                } catch (e: Exception) {
                    -1
                }
                mDialogListener!!.onUpdate(mPosition,
                    AddItem(resourceLabelId = R.drawable.btn_food_label,
                        title = etName.text.toString(),
                        price = mPrice))
                dismiss()
            }

            ivDelete.setOnClickListener {
                mDialogListener!!.onDelete(mPosition)
                dismiss()
            }
        }
    }
}