package com.example.adds2.accounthelper

import android.widget.Toast
import com.example.adds2.MainActivity
import com.google.firebase.auth.FirebaseUser

class AccountHelper(private val mainActivity: MainActivity) {

     var isDataEmpty = true

    fun registerWithEmail(email: String, password: String) {
        fun isDataValid() = (email.isNotEmpty() && password.isNotEmpty())
        if (isDataValid()) {
            mainActivity.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user ?: throw Exception("user is null")
                        sendEmailVerification(user)
                        mainActivity.updateUi(user)
                    } else {
                        makeToast("Auth task failed")
                    }
                }
        } else {
            makeToast("Data is empty")
        }
    }

    fun signInWithEmail(email: String, password: String) {
        fun isDataValid() = (email.isNotEmpty() && password.isNotEmpty())
        if (isDataValid()) {
            isDataEmpty = false
            mainActivity.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result.user
                        mainActivity.updateUi(user)
                    } else {
                        makeToast("Sign task failed")
                    }
                }
        } else {
            isDataEmpty = true
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
        Toast.makeText(mainActivity, text, Toast.LENGTH_LONG).show()
    }
}