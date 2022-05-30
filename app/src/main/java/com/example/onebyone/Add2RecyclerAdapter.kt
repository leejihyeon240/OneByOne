package com.example.onebyone

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Add2RecyclerAdapter(private val context: Context, private val dataList: ArrayList<AddItem2>):
    RecyclerView.Adapter<Add2RecyclerAdapter.MyViewHolder>() {
    var drawable: Drawable? = null

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var titleTv: TextView = itemView.findViewById<TextView>(R.id.titleTv)
        private var priceTv: TextView = itemView.findViewById<TextView>(R.id.priceTv)
        private var cameraAddLabelButton: ImageView = itemView.findViewById<ImageView>(R.id.cameraAddLabelButton)

        fun bind(addItem2: AddItem2, context: Context) {

            titleTv.text = addItem2.title
            priceTv.text = addItem2.price
            Glide.with(itemView.context).load(addItem2.resourceLabelId).into(cameraAddLabelButton)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.add_recycler_item, parent, false)

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