package com.example.onebyone

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.onebyone.listener.ItemClickListener
import com.example.onebyone.util.toWon

class AddRecyclerAdapter(
    private val dataList: ArrayList<AddItem>,
    private var revisable: Boolean = true,
) :
    RecyclerView.Adapter<AddRecyclerAdapter.MyViewHolder>() {
    private var mListener: ItemClickListener? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var cbTitle = itemView.findViewById<CheckBox>(R.id.cb_label)
        private var tvPrice = itemView.findViewById<TextView>(R.id.tv_price)

        fun bind(position: Int) {
            val item = dataList[position]

            Log.d("position",item.toString())

            //체크박스 안 항목, 그림 변경
            cbTitle.apply {
                text = item.title
                buttonDrawable = cbTitle.context.getDrawable(item.resourceLabelId)
                cbTitle.background = cbTitle.context.getDrawable(R.drawable.daily_cell_green)
            }

            //가격 형식으로 변경
            tvPrice.text = item.price.toWon()


/*            //revisable이 false이면
            if (revisable) {
                item.isChecked = true
            }*/

            //처음 보일 때는 isChecked = false임
            cbTitle.isChecked = item.isChecked

            if (!cbTitle.isChecked)
                cbTitle.background = cbTitle.context.getDrawable(R.drawable.daily_cell)

            if (dataList[position].isChecked == false) {
                cbTitle.setOnClickListener {
                    dataList[position].isChecked = true
                    mListener?.onClick(dataList, position)
                    notifyDataSetChanged()
                }
            } else {
                cbTitle.setOnClickListener {
                    dataList[position].isChecked = false
                    mListener?.onClick(dataList, position)
                    notifyDataSetChanged()
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
                LayoutInflater.from(parent.context).inflate(R.layout.item_add_label, parent, false)
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

    //override fun getItemViewType(position: Int) = dataList[position].type

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