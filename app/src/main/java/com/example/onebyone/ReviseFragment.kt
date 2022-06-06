package com.example.onebyone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.onebyone.R
import com.example.onebyone.ConsumeTypeAdapter
import com.example.onebyone.listener.ConsumeTypeClickListener
import com.example.onebyone.listener.DialogListener
import com.example.onebyone.AddItem
import com.example.onebyone.ConsumeItem
import com.example.onebyone.util.toDigit
import com.google.android.flexbox.*

class ReviseFragment : DialogFragment() {
    enum class TYPE_REVISE {
        ADD, UPDATE, DELETE
    }

    private var mType = TYPE_REVISE.ADD.name

    private var mItem: AddItem? = null
    private var mPosition = -1

    var mDialogListener: DialogListener? = null
    private var rcvConsumeType: RecyclerView? = null
    private var mResId = -1

    companion object {
        @JvmStatic
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
            mType = it.getString("type")!!
            mItem = it.getParcelable("item")
            mPosition = it.getInt("position", -1)
        }

        with(view) {
            val etName = findViewById<EditText>(R.id.et_name)
            val etPrice = findViewById<EditText>(R.id.et_price)
            val ivRevise = findViewById<ImageView>(R.id.iv_revise)
            val ivDelete = findViewById<ImageView>(R.id.iv_delete)

            mItem?.let {
                etName.setText(it.title)
                etPrice.setText(it.price.toString())
            }

            ivRevise.setOnClickListener {
                if (mResId < 0) {
                    Toast.makeText(requireContext(), "소비 타입을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (etName.text.toString().isNullOrBlank()) {
                    Toast.makeText(requireContext(), "상품명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (etPrice.text.toString().isNullOrBlank()) {
                    Toast.makeText(requireContext(), "금액을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val mPrice = try {
                    etPrice.text.toString().replace(",", "").toInt()
                } catch (e: Exception) {
                    -1
                }

                when(mType){
                    TYPE_REVISE.ADD.name->{
                        mDialogListener!!.onAdd(AddItem(
                            resourceLabelId = mResId,
                            title = etName.text.toString(),
                            price = etPrice.text.toString().toDigit()))

                    }
                    TYPE_REVISE.UPDATE.name->{
                        mDialogListener!!.onUpdate(mPosition, AddItem(
                            resourceLabelId = mResId,
                            title = etName.text.toString(),
                            price = mPrice))
                    }
                }
                dismiss()
            }

            ivDelete.setOnClickListener {
                mDialogListener!!.onDelete(mPosition)
                dismiss()
            }

            rcvConsumeType = findViewById(R.id.rcv_consume_type)

            rcvConsumeType?.apply {
                layoutManager = FlexboxLayoutManager(context).apply {
                    justifyContent = JustifyContent.CENTER
                    alignItems = AlignItems.CENTER
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                }

                adapter = ConsumeTypeAdapter(
                    listOf(
                        ConsumeItem(R.drawable.cb_add_food, "식품"),
                        ConsumeItem(R.drawable.cb_add_clothing, "의류/잡화"),
                        ConsumeItem(R.drawable.cb_add_necessity, "생필품"),
                        ConsumeItem(R.drawable.cb_add_cosmetics, "화장품"),
                        ConsumeItem(R.drawable.cb_add_appliances, "가전"),
                        ConsumeItem(R.drawable.cb_add_health, "건강/의료용품"),
                        ConsumeItem(R.drawable.cb_add_homedeco, "홈데코"),
                        ConsumeItem(R.drawable.cb_add_etc, "기타"),
                    )
                ).apply {
                    setListener(object : ConsumeTypeClickListener {
                        override fun onSelect(data: List<ConsumeItem>, position: Int) {
                            mResId = data[position].resId
                        }
                    })
                }
            }
        }
    }
}