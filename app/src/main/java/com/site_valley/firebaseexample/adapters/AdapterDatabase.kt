/*
 * Created by abhinav for ETAOP Pvt. Ltd.
 * Copyright (c) 2021 . All rights reserved.
 * Last Updated on 27/4/21 11:24 AM
 */

package com.site_valley.firebaseexample.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.site_valley.firebaseexample.R

class AdapterDatabase(val list: ArrayList<String>):
    RecyclerView.Adapter<AdapterDatabase.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view
            .findViewById(R.id.data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_with_text,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=list[position]
        holder.textView.text = data
    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun addItem(string: String) {
        list.add(string)
        notifyDataSetChanged()
    }

}