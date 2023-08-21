package com.example.adds2.accounthelper

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.example.adds2.App
import com.example.adds2.R
import com.example.adds2.needCloseTheDialog
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

private const val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
private const val TAG = "GoogleHelper"

class GoogleHelper(private val activity: AppCompatActivity) {

//    var needCloseTheDialog = true

    private val oneTapClient = Identity.getSignInClient(activity)

    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(activity.getString(R.string.your_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    fun oneTapSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(activity) { result ->
                try {
                    startIntentSenderForResult(
                        activity,
                        result.pendingIntent.intentSender,
                        REQ_ONE_TAP,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(activity) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, "addOnFailureListener Holy shit: ${e.localizedMessage}")
            }
    }

    fun onActivityResultUtil(requestCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            Log.d(TAG, "Got ID token.")
                            // Use token to get firebaseCredential
                            val firebaseCredential = GoogleAuthProvider
                                .getCredential(idToken, null)

                            singInWithCredential(firebaseCredential)
                        }

                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.d(TAG, "ApiException Holy shit: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun singInWithCredential(firebaseCredential: AuthCredential) {
        App.firebaseAuth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    App.activityListenerLambda?.invoke()
                    needCloseTheDialog = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // updateUI(null)
                    needCloseTheDialog = false
                }
            }
    }

}

