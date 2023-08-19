package com.example.adds2.dialoghelper

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.adds2.MainActivity
import com.example.adds2.accounthelper.EmailHelper
import com.example.adds2.accounthelper.GoogleHelper
import com.example.adds2.databinding.LogInDialogBinding
import com.example.adds2.databinding.RegisterDialogBinding
import com.example.adds2.makeToast

class DialogHelper(private val mainActivity: MainActivity) {

    private val emailHelper = EmailHelper(mainActivity)
    private val googleHelper = GoogleHelper(mainActivity)
    var drawerListener: DrawerListener? = null

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
            RegisterDialogBinding.inflate(LayoutInflater.from(mainActivity))

        DialogHelperConstants.LOG_IN_STATE ->
            LogInDialogBinding.inflate(LayoutInflater.from(mainActivity))

        else -> throw Exception("Unknown state: $state")
    }

    private fun createAlertDialog(binding: ViewBinding) = AlertDialog
        .Builder(mainActivity)
        .setView(binding.root)
        .setCancelable(false)
        .setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            drawerListener?.drawerAction()
        }
        .create()

    private fun setUpButtonsClickListeners(binding: ViewBinding, alertDialog: AlertDialog) {
        when (binding) {
            is RegisterDialogBinding -> {
                setUpBtnRegisterListener(binding, alertDialog)
            }

            is LogInDialogBinding -> {
                setUpBtnSignInOneTapClickListener(binding, alertDialog)
                setUpBtnLogInClickListener(binding, alertDialog)
                setUpBtnForgotPasswordClickListener(binding)
            }
        }
    }

    private fun setUpBtnSignInOneTapClickListener(
        binding: LogInDialogBinding,
        alertDialog: AlertDialog
    ) {
        binding.btnSignInOneTap.setOnClickListener {
            googleHelper.oneTapSignIn()
            closeDialogIfPossible(alertDialog)
        }
    }

    private fun setUpBtnForgotPasswordClickListener(binding: LogInDialogBinding) {
        binding.btnForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (email.isNotEmpty()) {
                mainActivity.auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        makeToast(
                            mainActivity,
                            "sendPasswordResetEmail request is Successful"
                        )
                    } else {
                        makeToast(
                            mainActivity,
                            "sendPasswordResetEmail request is Failed"
                        )
                    }
                }
            } else {
                makeToast(
                    mainActivity,
                    "email must be at least blablabla..."
                )
            }
        }
    }

    private fun setUpBtnLogInClickListener(binding: LogInDialogBinding, alertDialog: AlertDialog) {
        with(binding) {
            btnLogIn.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                emailHelper.signInWithEmail(email, password)
                closeDialogIfPossible(alertDialog)
            }
        }
    }

    private fun setUpBtnRegisterListener(binding: RegisterDialogBinding, alertDialog: AlertDialog) {
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
        if (emailHelper.needCloseTheDialog) {
            alertDialog.dismiss()
            drawerListener?.drawerAction()
        }
    }

}