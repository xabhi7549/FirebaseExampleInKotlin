/*
 * Created by abhinav for ETAOP Pvt. Ltd.
 * Copyright (c) 2021 . All rights reserved.
 * Last Updated on 27/4/21 12:55 PM
 */

package com.site_valley.firebaseexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.site_valley.firebaseexample.database.DatabaseActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MaterialButton>(R.id.database).setOnClickListener {
            startActivity(Intent(this,DatabaseActivity::class.java))
        }
    }
}