package com.example.colorbuddy.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.view.menu.MenuView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.classes.Item
import com.example.colorbuddy.R
import com.google.firebase.database.collection.LLRBNode
import kotlinx.android.synthetic.main.item_row.view.*




class ItemAdapter(val itemList: MutableList<Item>): RecyclerView.Adapter<ItemViewHolder>() {

    private val mItems = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(com.example.colorbuddy.R.layout.item_row,parent,false)
        return ItemViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
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
            holder.view.itemC1.setBackgroundColor(Color.parseColor(item.c1))
            holder.view.itemC1.layoutParams = param
            weight + 1
        }
        if(item.c2!="") {
            holder.view.itemC2.setBackgroundColor(Color.parseColor(item.c2))
            holder.view.itemC2.layoutParams = param
            weight + 1
        }
        if(item.c3!="") {
            holder.view.itemC3.setBackgroundColor(Color.parseColor(item.c3))
            holder.view.itemC3.layoutParams = param
            weight + 1
        }
        if(item.c4!="") {
            holder.view.itemC4.setBackgroundColor(Color.parseColor(item.c4))
            holder.view.itemC4.layoutParams = param
            weight + 1
        }
        if(item.c5!="") {
            holder.view.itemC5.setBackgroundColor(Color.parseColor(item.c5))
            holder.view.itemC5.layoutParams = param
            weight + 1
        }
        holder.view.itemPalette.weightSum = weight.toFloat()
    }
}

class ItemViewHolder(val view: View):RecyclerView.ViewHolder(view){

}