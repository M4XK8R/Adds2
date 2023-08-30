package com.example.adds2

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class App : Application() {

    companion object {
        //        lateinit var app: Application
        lateinit var firebaseAuth: FirebaseAuth
        var activityListenerLambda: (() -> Unit)? = null
    }

    override fun onCreate() {
        super.onCreate()
//        app = Application()
        firebaseAuth = Firebase.auth
    }
}