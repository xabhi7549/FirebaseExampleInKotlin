/*
 * Created by abhinav for ETAOP Pvt. Ltd.
 * Copyright (c) 2021 . All rights reserved.
 * Last Updated on 6/5/21 6:51 PM
 */

package com.site_valley.firebaseexample.database

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.site_valley.firebaseexample.R
import com.site_valley.firebaseexample.adapters.AdapterDatabase
import com.site_valley.firebaseexample.helpers.Constants

class DatabaseInsertDeleteActivity : AppCompatActivity() {
    lateinit var adapter: AdapterDatabase
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_insert_delete)
        val recyclerView = findViewById<RecyclerView>(R.id.rvDisplay)
        progressBar = findViewById(R.id.pBar)

        //Creating a empty adapter and assign it to recycler view
        adapter = AdapterDatabase(ArrayList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        setClicks()

        //Deleting Data On StartUp for clean UI
        deleteDataOfNode(Constants.dataRef)

    }


    private fun setClicks() {
        findViewById<MaterialButton>(R.id.insert).setOnClickListener {

            //Check If Data in editText is not null
            if (!findViewById<TextInputEditText>(R.id.insertData).text.isNullOrEmpty()) {
                insertData(findViewById<TextInputEditText>(R.id.insertData).text.toString())
                findViewById<TextInputEditText>(R.id.insertData).setText("")
            } else {
                Toast.makeText(this, "Please enter something!", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<MaterialButton>(R.id.fetch).setOnClickListener {
            //Check If Data in editText is not null
            fetchDataOfNode(Constants.dataRef)
        }
    }


    private fun insertData(string: String) {
        //It will automatically create a key and push data into it.
        Constants.dataRef.push().setValue(string)
        //To insert data in your defined key use this
        //Constants.dataRef.child("yourKey").setValue(string)
    }

    private fun fetchDataOfNode(databaseReference: DatabaseReference) {
        //Adding listener to database to read data
        progressBar.visibility = View.VISIBLE
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                adapter.list.clear()
                //Checking If Node Have Some Data
                if (snapshot.hasChildren()) {
                    for (child in snapshot.children) {
                        Log.e("Child Data", child.getValue(String::class.java).toString())
                        //Adding data to adapter.
                        adapter.addItem(child.getValue(String::class.java).toString())

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                //Read failed Maybe because of network or permission check Log
                Log.e("Database Read Error", error.message)
            }

        })

    }

    private fun deleteDataOfNode(databaseReference: DatabaseReference) {
        databaseReference.setValue(null)
    }
}