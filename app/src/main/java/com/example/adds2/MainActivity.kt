package com.example.adds2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.adds2.databinding.ActivityMainBinding
import com.example.adds2.dialoghelper.DialogHelper
import com.example.adds2.dialoghelper.DialogHelperConstants
import com.example.adds2.dialoghelper.DrawerListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tvAccount: TextView

    private var firebaseUser: FirebaseUser? = null
    private val dialogHelper = DialogHelper(this)

    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val header = binding.drawer.getHeaderView(0)
        tvAccount = header.findViewById(R.id.tvAccountEmail)

        setUpDrawer()
        setUpDrawerListenerInterface()
    }

    override fun onStart() {
        super.onStart()
        firebaseUser = auth.currentUser
        updateUi(firebaseUser)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.diMyAdds -> makeToastNavTest("diMyAdds")
            R.id.diCars -> makeToastNavTest("diCars")
            R.id.diComputers -> makeToastNavTest("diComputers")
            R.id.diSmartphones -> makeToastNavTest("diSmartphones")
            R.id.diAppliances -> makeToastNavTest("diAppliances")
            R.id.diRegister -> launchRegisterDialog()
            R.id.diLogIn -> launchSignInDialog()
            R.id.diLogOut -> {
                auth.signOut()
                updateUi(null)
            }

            else -> return true
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateUi(firebaseUser: FirebaseUser?) {
        tvAccount.text = if (firebaseUser == null) "not logged in" else firebaseUser.email
    }


    /**
     * PRIVATE FUNCTIONS
     */

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