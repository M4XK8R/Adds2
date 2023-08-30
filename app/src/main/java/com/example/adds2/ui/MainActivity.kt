package com.example.adds2.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.adds2.App
import com.example.adds2.R
import com.example.adds2.accounthelper.EmailHelper
import com.example.adds2.accounthelper.GoogleHelper
import com.example.adds2.databinding.ActivityMainBinding
import com.example.adds2.dialoghelper.DialogHelper
import com.example.adds2.dialoghelper.DialogHelperConstants
import com.example.adds2.ui.screens.search.fragment.AddsFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import kotlin.random.Random

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tvAccount: TextView

    private lateinit var dialogHelper: DialogHelper

    private lateinit var googleHelper: GoogleHelper
    private lateinit var emailHelper: EmailHelper

    private val firebaseUser: FirebaseUser? get() = App.firebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialogHelper = DialogHelper(this)
        googleHelper = GoogleHelper(this)
        emailHelper = EmailHelper(this)

        val header = binding.drawer.getHeaderView(0)
        tvAccount = header.findViewById(R.id.tvAccountEmail)
        App.activityListenerLambda = { updateUi(firebaseUser) }

        setUpDrawer()
        dialogHelper.drawerListenerLambda = ::openDrawer

    }

    override fun onStart() {
        super.onStart()
        updateUi(firebaseUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleHelper.onActivityResultUtil(requestCode, data)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.diMyAdds -> {
                launchAddsFragment()
                closeDrawer()
            }

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
                App.firebaseAuth.signOut()
                updateUi(null)
            }

            else -> return true
        }
        return true
    }

    /**
     * PRIVATE FUNCTIONS
     */
    private fun launchAddsFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, AddsFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun updateUi(firebaseUser: FirebaseUser?) {
        val error = Random.nextInt(405)
        tvAccount.text = if (firebaseUser == null) "Not logged in #$error" else firebaseUser.email
    }

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