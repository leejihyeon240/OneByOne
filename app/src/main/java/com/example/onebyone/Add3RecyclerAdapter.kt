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

class Add3RecyclerAdapter(
    private val dataList: ArrayList<AddItem>,
    private val revisable: Boolean = false,
) :
    RecyclerView.Adapter<Add3RecyclerAdapter.MyViewHolder>() {
    private var mListener: ItemClickListener? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvPrice = itemView.findViewById<TextView>(R.id.tv_price)
        private var cbLabel = itemView.findViewById<CheckBox>(R.id.cb_label)

        fun bind(position: Int) {
            val item = dataList[position]
            cbLabel.apply {
                buttonDrawable = cbLabel.context.getDrawable(item.resourceLabelId)
                text = item.title
            }

            tvPrice.text = item.price.toWon()
            if (revisable) {
                item.isChecked = true
            }
            cbLabel.isChecked = item.isChecked


            if (revisable) {
                cbLabel.setOnCheckedChangeListener { compoundButton, b ->
                    cbLabel.isChecked = true
                }
                cbLabel.setOnClickListener {
                    mListener?.onClick(dataList, position)
                }
            } else {
                cbLabel.setOnCheckedChangeListener { compoundButton, b ->
                    try {
                        dataList[position].isChecked = cbLabel.isChecked
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
                LayoutInflater.from(parent.context).inflate(R.layout.activity_camera_add_sale_item, parent, false)
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