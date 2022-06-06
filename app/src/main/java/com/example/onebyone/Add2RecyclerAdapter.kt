package com.example.onebyone

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class Add2RecyclerAdapter(private val context: Context, private val dataList: ArrayList<AddItem2>):
    RecyclerView.Adapter<Add2RecyclerAdapter.MyViewHolder>() {
    var drawable: Drawable? = null

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var cbTitle = itemView.findViewById<CheckBox>(R.id.cb_label)
        private var tvPrice = itemView.findViewById<TextView>(R.id.tv_price)

        fun bind(addItem2: AddItem2, context: Context) {
            cbTitle.text = addItem2.title
            tvPrice.text = addItem2.price
            cbTitle.apply {
                buttonDrawable = cbTitle.context.getDrawable(addItem2.resourceLabelId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_add_recycler, parent, false)

        drawable = ContextCompat.getDrawable(view.context, R.drawable.cameraadd_label_button)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position], context)

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount(): Int {
        return dataList.size
    }

}