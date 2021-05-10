package com.veen.homechef.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.veen.homechef.R
import com.veen.homechef.Adapter.NetworkConnection
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemReselectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                layoutdisconnected.visibility = View.GONE
                navView = findViewById(R.id.nav_view)
                navView.setOnNavigationItemSelectedListener(this)
            } else {
                layoutdisconnected.visibility = View.VISIBLE
            }
        })
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        Log.d("TAG", "onNavigationItemReselected: Failed")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val getsaveloginID = AppUtils.getsaveloginID(applicationContext)
        when (item.itemId) {
            R.id.nav_dash -> {
                this.supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, MainFragment())
//                    .addToBackStack("MainFragment")
                    .commit()
            }
            R.id.nav_home -> {
                if (getsaveloginID != "") {
                    this.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment())
//                        .addToBackStack("HomeFragment")
                        .commit()
                } else {
                    startActivity(Intent(this, Login::class.java))
                }
            }
            R.id.nav_cart -> {
                if (getsaveloginID != "") {
                    this.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, CartFragment())
//                        .addToBackStack("CartFragment")
                        .commit()
                } else {
                    startActivity(Intent(this, Login::class.java))
                }
            }
            R.id.nav_user -> {
                if (getsaveloginID != "") {
                    this.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, ProfileFragment())
//                        .addToBackStack("ProfileFragment")
                        .commit()
                } else {
                    startActivity(Intent(this, Login::class.java))
                }
            }
        }
            return true
    }

//    override fun onBackPressed() {
//        val alertDialogBuilder = AlertDialog.Builder(this)
//        alertDialogBuilder.setTitle(R.string.app_name)
//        alertDialogBuilder
//            .setMessage("Are you sure you want to exit")
//            .setCancelable(false)
//            .setPositiveButton("Yes") { dialog, id -> // if this button is clicked, close
//                val a = Intent(Intent.ACTION_MAIN)
//                a.addCategory(Intent.CATEGORY_HOME)
//                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(a)
//            }
//            .setNegativeButton("No") { dialog, id -> // if this button is clicked, just closex
//                dialog.cancel()
//            }
//
//        val alertDialog = alertDialogBuilder.create()
//
//        alertDialog.show()
//    }
}