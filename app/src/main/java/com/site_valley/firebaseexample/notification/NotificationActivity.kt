/*
 * Created by abhinav for ETAOP Pvt. Ltd.
 * Copyright (c) 2021 . All rights reserved.
 * Last Updated on 10/06/21, 12:05 PM
 */

package com.site_valley.firebaseexample.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.messaging.FirebaseMessaging
import com.site_valley.firebaseexample.MainActivity
import com.site_valley.firebaseexample.R
import org.json.JSONException
import org.json.JSONObject


class NotificationActivity : AppCompatActivity() {
    var token: String? = null
    lateinit var title: TextInputEditText
    lateinit var body: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        getFirebaseFCMToken()
        registerChannel("default")

        title = findViewById(R.id.title)
        body = findViewById(R.id.body)

        setListeners()

    }

    private fun registerChannel(channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(channelName, channelName, NotificationManager.IMPORTANCE_HIGH)
            )
        }
    }

    private fun setListeners() {
        findViewById<MaterialButton>(R.id.send_local_notification).setOnClickListener {
            if (notifyTitleBodyCheck()) {
                sendNotificationLocal(title.text.toString(), body.text.toString())
            } else {
                Toast.makeText(this, "Title And Body Is Req.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<MaterialButton>(R.id.send_firebase_notification).setOnClickListener {
            if (notifyTitleBodyCheck()) {
                if (token != null) {
                    sendNotificationFirebase(title.text.toString(), body.text.toString(), token!!)
                } else {
                    Toast.makeText(
                        this,
                        "Token Not Available. Check You network and firebase integration!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Title And Body Is Req.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun notifyTitleBodyCheck(): Boolean {
        return !(title.text.isNullOrBlank() || body.text.isNullOrBlank())
    }

    private fun sendNotificationLocal(title: String, body: String) {

        val intent = Intent(this, MainActivity::class.java)
        val contentIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val b = NotificationCompat.Builder(this, "default")

        b.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .setContentIntent(contentIntent)
            .setContentInfo("Info")
        NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), b.build())

    }

     fun getFirebaseFCMToken() {
         FirebaseMessaging.getInstance().token.addOnCompleteListener {
             token = it.result
             findViewById<MaterialTextView>(R.id.token).text = "FCM Token : $token"
         }
     }


    private fun sendNotificationFirebase(title: String, msg: String, token: String) {
        val SERVER_KEY = "" //TODO: YOUR_SERVER_KEY
        var obj: JSONObject? = null
        var objData: JSONObject?
        var dataobjData: JSONObject? = null
        try {
            obj = JSONObject()
            objData = JSONObject()
            objData.put("body", msg)
            objData.put("title", title)
            objData.put("sound", "default")
            objData.put("tag", token)
            objData.put("priority", "high")
            dataobjData = JSONObject()
            dataobjData.put("text", msg)
            dataobjData.put("title", title)
            obj.put("to", token)
            obj.put("notification", objData)
            obj.put("data", dataobjData)
            Log.e("return here>>", obj.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsObjRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                Response.Listener { response -> Log.e("True", response.toString() + "") },
                Response.ErrorListener { error -> Log.e("False", error.toString() + "") }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] = "key=$SERVER_KEY"
                    params["Content-Type"] = "application/json"
                    return params
                }
            }
        val requestQueue = Volley.newRequestQueue(this)
        val socketTimeout = 1000 * 60 // 60 seconds
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsObjRequest.retryPolicy = policy
        requestQueue.add(jsObjRequest)
    }
}