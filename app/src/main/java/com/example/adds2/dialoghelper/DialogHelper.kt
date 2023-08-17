package com.example.adds2.dialoghelper

import android.app.AlertDialog
import android.view.LayoutInflater
import com.example.adds2.MainActivity
import com.example.adds2.accounthelper.AccountHelper
import com.example.adds2.databinding.RegisterDialogBinding
import com.example.adds2.databinding.SignDialogBinding

class DialogHelper(private val mainActivity: MainActivity) {

    private val accountHelper = AccountHelper(mainActivity)
    var drawerListener: DrawerListener? = null

    fun createDialog(state: Int) {
        val binding = when (state) {
            DialogHelperConstants.REGISTER_STATE ->
                RegisterDialogBinding.inflate(LayoutInflater.from(mainActivity))

            DialogHelperConstants.LOG_IN_STATE ->
                SignDialogBinding.inflate(LayoutInflater.from(mainActivity))

            else -> throw Exception("Unknown state: $state")
        }

        val alertDialog = AlertDialog.Builder(mainActivity)
            .setView(binding.root)
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
                drawerListener?.drawerAction()
            }
            .create()

        alertDialog.show()

        if (binding is RegisterDialogBinding) {
            binding.btnRegister.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                accountHelper.registerWithEmail(email, password)
                closeDialogIfPossible(alertDialog)
            }
        }

        if (binding is SignDialogBinding) {
            binding.btnSignIn.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                accountHelper.signInWithEmail(email, password)
                closeDialogIfPossible(alertDialog)
            }
        }
    }

    private fun closeDialogIfPossible(alertDialog: AlertDialog) {
        if (!accountHelper.isDataEmpty) {
            alertDialog.dismiss()
            drawerListener?.drawerAction()
        }
    }

}