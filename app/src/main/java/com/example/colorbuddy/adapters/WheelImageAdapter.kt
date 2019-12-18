package com.example.colorbuddy.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import com.example.colorbuddy.Data.ImageData
import com.example.colorbuddy.R
import github.hellocsl.cursorwheel.CursorWheelLayout

class WheelImageAdapter(val mContext: Context, val colors: List<ImageData>): CursorWheelLayout.CycleWheelAdapter(){

    private val inflater:LayoutInflater

    init{
        inflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return colors.size
    }

    override fun getView(parent: View?, position: Int): View {
        val data = getItem(position)
        val root = inflater.inflate(R.layout.colorwheellayout,null,false)
        val imageView = root.findViewById<View>(R.id.colorImageView)
        imageView.setBackgroundColor(Color.parseColor(data.imageDescription))
        return root
    }

    override fun getItem(position: Int): ImageData {
        return colors[position]
    }
}