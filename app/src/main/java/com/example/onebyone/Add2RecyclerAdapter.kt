package com.example.onebyone

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onebyone.listener.ItemClickListener
import com.example.onebyone.util.toWon

class Add2RecyclerAdapter(
    private val dataList: ArrayList<AddItem>,
    private val revisable: Boolean = false,
) :
    RecyclerView.Adapter<Add2RecyclerAdapter.MyViewHolder>() {
    private var mListener: ItemClickListener? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var cbTitle = itemView.findViewById<CheckBox>(R.id.cb_label)
        private var tvPrice = itemView.findViewById<TextView>(R.id.tv_price)

        fun bind(position: Int) {
            val item = dataList[position]
            cbTitle.apply {
                text = item.title
                buttonDrawable = cbTitle.context.getDrawable(item.resourceLabelId)
            }
            tvPrice.text = item.price.toWon()

            if (revisable) {
                item.isChecked = true
            }
            cbTitle.isChecked = item.isChecked


            if (revisable) {
                cbTitle.setOnClickListener {
                    dataList[position].isChecked = true
                    mListener?.onClick(dataList, position)
                    notifyDataSetChanged()
                }
            } else {
                cbTitle.setOnCheckedChangeListener { compoundButton, b ->
                    try {
                        dataList[position].isChecked = cbTitle.isChecked
                        mListener?.onClick(dataList, position)
                        notifyDataSetChanged()
                    }catch (e: java.lang.Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val view = when (viewType) {
            1 -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_add_result, parent, false)
            }
            else -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_add_label, parent, false)
            }
        }
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int) = dataList[position].type

    override fun getItemCount() = dataList.size

    fun setListener(listener: ItemClickListener) {
        mListener = listener
    }

    fun selectAll(boolean: Boolean) {
        dataList.map {
            it.isChecked = boolean
        }
        notifyDataSetChanged()
    }

    fun addItem(item: AddItem) {
        Log.d("add", "item: ${item.toString()}")
        dataList.add(item)
        notifyDataSetChanged()
    }

    fun updateItem(item: AddItem, position: Int) {
        try {
            dataList[position] = item
            notifyItemChanged(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteItem(position: Int) {
        if (position < 0) return
        try {
            dataList.removeAt(position)
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getList() = dataList
}