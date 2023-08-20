package com.example.adds2.accounthelper

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.example.adds2.R
import com.example.adds2.dialoghelper.ActivityListener
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException

private const val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
private const val TAG = "GoogleHelper"

class GoogleHelper(private val activity: AppCompatActivity) {

    var activityListener: ActivityListener? = null

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
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            Log.d(TAG, "Got ID token.")
                            activityListener?.updateUi(username)
//
                        }

                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }

                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.d(TAG, "ApiException Holy shit: ${e.localizedMessage}")
                }
            }
        }
    }
}

