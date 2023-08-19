package com.example.adds2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.adds2.accounthelper.GoogleHelper
import com.example.adds2.databinding.ActivityMainBinding
import com.example.adds2.dialoghelper.DialogHelper
import com.example.adds2.dialoghelper.DialogHelperConstants
import com.example.adds2.dialoghelper.DrawerListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tvAccount: TextView

    private var firebaseUser: FirebaseUser? = null

    private lateinit var dialogHelper: DialogHelper
    private lateinit var googleHelper: GoogleHelper

    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        dialogHelper = DialogHelper(this)
        googleHelper = GoogleHelper(this)

        val header = binding.drawer.getHeaderView(0)
        tvAccount = header.findViewById(R.id.tvAccountEmail)

        setUpDrawer()
        setUpDrawerListenerInterface()

        firebaseUser = auth.currentUser
        var currentUser = auth.getCurrentUser()
    }

    override fun onStart() {
        super.onStart()
        firebaseUser = auth.currentUser
        Log.d(TAG, "firebaseUser = $firebaseUser")
        updateUi(firebaseUser)
//        updateUi(googleHelper.)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleHelper.onActivityResultUtil(requestCode, data)
        Log.d(TAG, "firebaseUser = $firebaseUser")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.diMyAdds -> makeToastNavTest("diMyAdds")
            R.id.diCars -> makeToastNavTest("diCars")
            R.id.diComputers -> makeToastNavTest("diComputers")
            R.id.diSmartphones -> makeToastNavTest("diSmartphones")
            R.id.diAppliances -> closeDrawer()
            R.id.diRegister -> {
                launchRegisterDialog()
                closeDrawer()
            }

            R.id.diLogIn -> {
                launchSignInDialog()
                closeDrawer()
            }

            R.id.diLogOut -> {
                auth.signOut()
                updateUi(null)
            }

            else -> return true
        }
        return true
    }

    fun updateUi(firebaseUser: FirebaseUser?) {
        tvAccount.text = if (firebaseUser == null) "not logged in" else firebaseUser.email
    }

    fun updateUi(text: String) {
        tvAccount.text = text
    }

    /**
     * PRIVATE FUNCTIONS
     */
    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun launchSignInDialog() {
        dialogHelper.setUpAlertDialog(DialogHelperConstants.LOG_IN_STATE)
    }

    private fun launchRegisterDialog() {
        dialogHelper.setUpAlertDialog(DialogHelperConstants.REGISTER_STATE)
    }

    private fun makeToastNavTest(id: String) {
        Toast.makeText(this, "Pressed $id", Toast.LENGTH_SHORT).show()
    }

    private fun setUpDrawerListenerInterface() {
        dialogHelper.drawerListener = object : DrawerListener {
            override fun drawerAction() {
                openDrawer()
            }
        }
    }

    private fun setUpDrawer() {
        val drawer = binding.drawerLayout
        val toolbar = binding.contentMain.toolbar
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.drawer.setNavigationItemSelectedListener(this)
        openDrawer()
    }

    private fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

}