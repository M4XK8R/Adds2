package com.example.adds2.dialoghelper

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.adds2.App
import com.example.adds2.accounthelper.EmailHelper
import com.example.adds2.accounthelper.GoogleHelper
import com.example.adds2.databinding.DialogLogInBinding
import com.example.adds2.databinding.DialogRegisterBinding
import com.example.adds2.utils.makeToast
import com.example.adds2.utils.needCloseTheDialog

class DialogHelper (private val activity: AppCompatActivity) {

    private val emailHelper = EmailHelper(activity)
    private val googleHelper = GoogleHelper(activity)

    var drawerListenerLambda: (() -> Unit)? = null

    fun setUpAlertDialog(state: Int) {
        val binding = initBinding(state)

        val alertDialog = createAlertDialog(binding)
        alertDialog.show()

        setUpButtonsClickListeners(binding, alertDialog)
    }

    /**
     * PRIVATE FUNCTIONS
     */
    private fun initBinding(state: Int) = when (state) {
        DialogHelperConstants.REGISTER_STATE ->
            DialogRegisterBinding.inflate(LayoutInflater.from(activity))

        DialogHelperConstants.LOG_IN_STATE ->
            DialogLogInBinding.inflate(LayoutInflater.from(activity))

        else -> throw Exception("Unknown state: $state")
    }

    private fun createAlertDialog(binding: ViewBinding) = AlertDialog.Builder(activity)
        .setView(binding.root)
        .setCancelable(false)
        .setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            Log.d(
                "drawerListenerLambda",
                "Create dialog drawerListenerLambda = $drawerListenerLambda"
            )
            drawerListenerLambda?.invoke()
        }
        .create()

    private fun setUpButtonsClickListeners(binding: ViewBinding, alertDialog: AlertDialog) {
        when (binding) {
            is DialogRegisterBinding -> {
                setUpBtnRegisterListener(binding, alertDialog)
            }

            is DialogLogInBinding -> {
                setUpBtnSignInOneTapClickListener(binding, alertDialog)
                setUpBtnLogInClickListener(binding, alertDialog)
                setUpBtnForgotPasswordClickListener(binding)
            }
        }
    }

    private fun setUpBtnSignInOneTapClickListener(
        binding: DialogLogInBinding,
        alertDialog: AlertDialog
    ) {
        binding.btnSignInOneTap.setOnClickListener {
            googleHelper.oneTapSignIn()
            closeDialogIfPossible(alertDialog)
        }
    }

    private fun setUpBtnForgotPasswordClickListener(binding: DialogLogInBinding) {
        binding.btnForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (email.isNotEmpty()) {
                App.firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        makeToast(
                            activity,
                            "sendPasswordResetEmail request is Successful"
                        )
                    } else {
                        makeToast(
                            activity,
                            "sendPasswordResetEmail request is Failed"
                        )
                    }
                }
            } else {
                makeToast(
                    activity,
                    "email must be at least blablabla..."
                )
            }
        }
    }

    private fun setUpBtnLogInClickListener(binding: DialogLogInBinding, alertDialog: AlertDialog) {
        with(binding) {
            btnLogIn.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                emailHelper.signInWithEmail(email, password)
                closeDialogIfPossible(alertDialog)
            }
        }
    }

    private fun setUpBtnRegisterListener(binding: DialogRegisterBinding, alertDialog: AlertDialog) {
        with(binding) {
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                emailHelper.registerWithEmail(email, password)
                closeDialogIfPossible(alertDialog)
            }
        }
    }

    private fun closeDialogIfPossible(alertDialog: AlertDialog) {
        if (needCloseTheDialog) {
            alertDialog.dismiss()
            drawerListenerLambda?.invoke()
        }
    }

}