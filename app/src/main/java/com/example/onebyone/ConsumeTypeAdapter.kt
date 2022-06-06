package com.example.onebyone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.onebyone.listener.ConsumeTypeClickListener

class ConsumeTypeAdapter(val data: List<ConsumeItem>) :
    RecyclerView.Adapter<ConsumeTypeAdapter.MyViewHolder>() {

    private var mListener: ConsumeTypeClickListener? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var cbConsumeType = itemView.findViewById<CheckBox>(R.id.cb_consume_type)

        fun bind(item: ConsumeItem, context: Context) {
            cbConsumeType.apply {
                background = context.getDrawable(item.resId)
                isChecked = item.isSelected
                setOnClickListener {
                    data.forEach {
                        it.isSelected = false
                    }
                    data[position].isSelected = true
                    cbConsumeType.isChecked = true
                    mListener?.onSelect(data, position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consume_type, parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            bind(data[position], holder.itemView.context)
        }
    }

    override fun getItemCount() = data.size

    fun setListener( listener: ConsumeTypeClickListener){
        mListener = listener
    }
}