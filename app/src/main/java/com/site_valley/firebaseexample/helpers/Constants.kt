/*
 * Created by abhinav for ETAOP Pvt. Ltd.
 * Copyright (c) 2021 . All rights reserved.
 * Last Updated on 26/4/21 7:46 PM
 */

package com.site_valley.firebaseexample.helpers

import com.google.firebase.database.FirebaseDatabase

object Constants{
    val realtimeDBRef=FirebaseDatabase.getInstance().reference.child("RealTimeDB")
}
