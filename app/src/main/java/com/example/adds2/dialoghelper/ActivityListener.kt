package com.example.adds2.dialoghelper

import com.google.firebase.auth.FirebaseUser

interface ActivityListener {

    fun updateUi(text: String)

    fun updateUi(firebaseUser: FirebaseUser?)
}