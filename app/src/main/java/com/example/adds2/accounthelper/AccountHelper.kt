package com.example.adds2.accounthelper

import android.widget.Toast
import com.example.adds2.MainActivity
import com.google.firebase.auth.FirebaseUser

class AccountHelper(private val mainActivity: MainActivity) {

    var needCloseTheDialog = false

    fun registerWithEmail(email: String, password: String) {
        fun isDataValid() = (email.isNotEmpty() && password.isNotEmpty())
        if (isDataValid()) {
            mainActivity.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user ?: throw Exception("user is null")
                        sendEmailVerification(user)
                        mainActivity.updateUi(user)
                        needCloseTheDialog = true
                    } else {
                        needCloseTheDialog = false
                        makeToast("Auth task failed")
                    }
                }
        } else {
            needCloseTheDialog = false
            makeToast("Data is empty")
        }
    }

    fun signInWithEmail(email: String, password: String) {
        fun isDataValid() = (email.isNotEmpty() && password.isNotEmpty())
        if (isDataValid()) {
            mainActivity.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result.user
                        mainActivity.updateUi(user)
                        needCloseTheDialog = true
                    } else {
                        needCloseTheDialog = false
                        makeToast("Sign task failed")
                    }
                }
        } else {
            needCloseTheDialog = false
            makeToast("Data is empty")
        }
    }

    private fun sendEmailVerification(firebaseUser: FirebaseUser) {
        firebaseUser.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                makeToast("EmailVerification is successful")
            } else {
                makeToast("EmailVerification is failed")
            }
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(mainActivity, text, Toast.LENGTH_SHORT).show()
    }
}