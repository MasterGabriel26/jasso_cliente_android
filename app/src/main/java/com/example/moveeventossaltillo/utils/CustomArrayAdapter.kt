package com.example.moveeventossaltillo.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomArrayAdapter(context: Context, resource: Int, objects: Array<String>) :
    ArrayAdapter<String>(context, resource, objects) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.setTextColor(Color.BLACK)
        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.setTextColor(Color.BLACK)
        return view
    }
}
