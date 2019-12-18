package com.example.colorbuddy.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Item
import kotlinx.android.synthetic.main.item_row_delete.view.*

class DeleteItemAdapter(val itemList: MutableList<Item>): RecyclerView.Adapter<DeleteItemsViewHolder>(){
    private val mItems = itemList

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteItemsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_row_delete,parent,false)
        return DeleteItemsViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DeleteItemsViewHolder, position: Int) {
        val item = mItems[position]
        val weight = 0
        val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f
        )
        holder.view.itemType.text = item.itemName
        holder.view.itemDescription.text = item.itemDescript
        if(item.c1!="") {
            holder.view.deleteItemC1.setBackgroundColor(Color.parseColor(item.c1))
            holder.view.deleteItemC1.layoutParams = param
            weight + 1
        }
        if(item.c2!="") {
            holder.view.deleteItemC2.setBackgroundColor(Color.parseColor(item.c2))
            holder.view.deleteItemC2.layoutParams = param
            weight + 1
        }
        if(item.c3!="") {
            holder.view.deleteItemC3.setBackgroundColor(Color.parseColor(item.c3))
            holder.view.deleteItemC3.layoutParams = param
            weight + 1
        }
        if(item.c4!="") {
            holder.view.deleteItemC4.setBackgroundColor(Color.parseColor(item.c4))
            holder.view.deleteItemC4.layoutParams = param
            weight + 1
        }
        if(item.c5!="") {
            holder.view.deleteItemC5.setBackgroundColor(Color.parseColor(item.c5))
            holder.view.deleteItemC5.layoutParams = param
            weight + 1
        }
        holder.view.itemPalette.weightSum = weight.toFloat()
    }
}

class DeleteItemsViewHolder(val view: View): RecyclerView.ViewHolder(view){

}