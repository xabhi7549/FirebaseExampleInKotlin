/*
 * Created by abhinav for ETAOP Pvt. Ltd.
 * Copyright (c) 2021 . All rights reserved.
 * Last Updated on 27/4/21 12:55 PM
 */

package com.site_valley.firebaseexample.database

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.site_valley.firebaseexample.R
import com.site_valley.firebaseexample.adapters.AdapterDatabase
import com.site_valley.firebaseexample.helpers.Constants

class DatabaseActivity : AppCompatActivity() {
    val databaseReference = Constants.realtimeDBRef
    lateinit var listener:ChildEventListener
    lateinit var adapter: AdapterDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        val editText=findViewById<TextInputEditText>(R.id.mesg)
        val recyclerView = findViewById<RecyclerView>(R.id.rv)

        //Clear All Mesg On StartUp
        clearRealTimeDB()

        //Creating a empty adapter and assign it to recycler view
        adapter = AdapterDatabase(ArrayList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        findViewById<MaterialButton>(R.id.save).setOnClickListener {
            // Checking if there is a text in editText or not
            if (editText.length()>0){
                saveToDb(editText.text.toString())
                editText.setText("")
            }else{
                Toast.makeText(this,"Please Enter A Mesg!",Toast.LENGTH_SHORT).show()
            }
        }

        setRealTimeListener()
    }

    private fun clearRealTimeDB() {
        //setting value to null to delete all mesg on startUp
        // You can also use this for deleting a entry
        databaseReference.setValue(null)
    }

    private fun setRealTimeListener() {
        // setting realtime listener to read node values
         listener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.e("DataOnChildAdded",snapshot.getValue(String::class.java).toString())
                adapter.addItem(snapshot.getValue(String::class.java).toString())
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
              //  TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
               // TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
              //  TODO("Not yet implemented")
            }

        }

        databaseReference.addChildEventListener(listener)
    }

    private fun saveToDb(string: String) {
        //Pushing the text to realtimeDBRef Node
        databaseReference.push().setValue(string)
    }

    override fun onDestroy() {
        databaseReference.removeEventListener(listener)
        super.onDestroy()
    }
}