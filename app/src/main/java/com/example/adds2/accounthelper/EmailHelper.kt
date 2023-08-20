package com.example.adds2.accounthelper

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adds2.App
import com.example.adds2.dialoghelper.ActivityListener
import com.google.firebase.auth.FirebaseUser

class EmailHelper(private val activity: AppCompatActivity) {

    var activityListener: ActivityListener? = null
    var needCloseTheDialog = true


    fun registerWithEmail(email: String, password: String) {
        fun isDataValid() = (email.isNotEmpty() && password.isNotEmpty())
        if (isDataValid()) {
            App.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user ?: throw Exception("user is null")
                        sendEmailVerification(user)
                        activityListener?.updateUi(user)
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
            App.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result.user
                       activityListener?.updateUi(user)
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
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}