package com.example.jobtracksa

import android.content.Context
import com.google.firebase.FirebaseApp

object FirebaseTest {

    fun isFirebaseInitialized(context: Context): Boolean {
        return FirebaseApp.getApps(context).isNotEmpty()
    }
}
