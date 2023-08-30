package com.example.adds2.accounthelper

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adds2.App
import com.example.adds2.utils.needCloseTheDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

class EmailHelper(private val activity: AppCompatActivity) {

//    var needCloseTheDialog = true

    fun registerWithEmail(email: String, password: String) {
        fun isDataValid() = (email.isNotEmpty() && password.isNotEmpty())
        if (isDataValid()) {
            App.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = task.result?.user ?: throw Exception("user is null")
                        sendEmailVerification(user)
                        Log.d(
                            "activityListenerLambda",
                            "Register activityListenerLambda = ${App.activityListenerLambda}"
                        )
                        App.activityListenerLambda?.invoke()
                        makeToast("Auth task is successful!")
                        needCloseTheDialog = true
                    } else {
//                        makeToast("Auth task failed")
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            makeToast("EMAIL ALREADY IN USE")
                            // Link email
                            val credential = EmailAuthProvider
                                .getCredential(email, password)
                            App.firebaseAuth.currentUser?.linkWithCredential(credential)
                                ?.addOnCompleteListener { linkTask ->
                                    if (linkTask.isSuccessful) {
                                        makeToast("TASK LINK EMAIL IS SUCCESSFUL")
                                    } else {
                                        makeToast("TASK LINK EMAIL IS FAILED")
                                    }
                                }
                        }
                        needCloseTheDialog = false
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
                        Log.d(
                            "activityListenerLambda",
                            "Sign in activityListenerLambda = ${App.activityListenerLambda}"
                        )
                        App.activityListenerLambda?.invoke()
                        makeToast("Auth task is successful!")
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