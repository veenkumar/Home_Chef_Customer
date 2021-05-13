package com.veen.homechef.Activity

import android.content.Intent
import android.media.MediaDrm
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.facebook.appevents.internal.AppEventUtility.bytesToHex
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.veen.homechef.Adapter.NetworkConnection
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.fragment.CartFragment
import com.veen.homechef.fragment.HomeFragment
import com.veen.homechef.fragment.MainFragment
import com.veen.homechef.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.util.*


class MainActivity : AppCompatActivity(),
        BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {

    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWidevineSN()

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
                this.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment())
//                        .addToBackStack("HomeFragment")
                        .commit()
            }
            R.id.nav_cart -> {
                this.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, CartFragment())
//                        .addToBackStack("CartFragment")
                        .commit()
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

    fun getWidevineSN(): String? {
        var sRet: String? = ""
        val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
        var mediaDrm: MediaDrm? = null
        try {
            mediaDrm = MediaDrm(WIDEVINE_UUID)
            val widevineId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            md.update(widevineId)
            sRet = bytesToHex(md.digest()) //we convert byte[] to hex for our purposes
        } catch (e: java.lang.Exception) {
            //WIDEVINE is not available
            Log.e("TAG", "getWidevineSN.WIDEVINE is not available")
            return null
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mediaDrm?.close()
            } else {
                mediaDrm?.release()
            }
        }
        var phoneuniqueID = sRet
        AppUtils.savePhoneUnique(applicationContext, phoneuniqueID)
        return sRet
    } //getWidevineSN
}

/*
override fun onBackPressed() {
val alertDialogBuilder = AlertDialog.Builder(this)
alertDialogBuilder.setTitle(R.string.app_name)
alertDialogBuilder
.setMessage("Are you sure you want to exit")
.setCancelable(false)
.setPositiveButton("Yes") { dialog, id -> // if this button is clicked, close
val a = Intent(Intent.ACTION_MAIN)
a.addCategory(Intent.CATEGORY_HOME)
a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
startActivity(a)
}
.setNegativeButton("No") { dialog, id -> // if this button is clicked, just closex
dialog.cancel()
}

val alertDialog = alertDialogBuilder.create()

alertDialog.show()
}
*/
