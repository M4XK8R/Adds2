package com.example.adds2.dialoghelper

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.adds2.MainActivity
import com.example.adds2.accounthelper.AccountHelper
import com.example.adds2.databinding.RegisterDialogBinding
import com.example.adds2.databinding.SignDialogBinding

class DialogHelper(private val mainActivity: MainActivity) {

    private val accountHelper = AccountHelper(mainActivity)
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
            SignDialogBinding.inflate(LayoutInflater.from(mainActivity))

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

    private fun setUpButtonsClickListeners(
        binding: ViewBinding,
        alertDialog: AlertDialog
    ) {
        if (binding is RegisterDialogBinding) {
            binding.btnRegister.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                accountHelper.registerWithEmail(email, password)
                Log.d(
                    "closeDialogIfPossible",
                    "accountHelper.needCloseTheDialog = ${accountHelper.needCloseTheDialog}"
                )
                closeDialogIfPossible(alertDialog)
            }
        }

        if (binding is SignDialogBinding) {
            binding.btnSignIn.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                accountHelper.signInWithEmail(email, password)
                Log.d(
                    "closeDialogIfPossible",
                    "accountHelper.needCloseTheDialog = ${accountHelper.needCloseTheDialog}"
                )
                closeDialogIfPossible(alertDialog)
            }
        }
    }

    private fun closeDialogIfPossible(alertDialog: AlertDialog) {
        if (accountHelper.needCloseTheDialog) {
            alertDialog.dismiss()
            drawerListener?.drawerAction()
        }
    }

}