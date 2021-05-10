package com.veen.homechef.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.veen.homechef.R

class Splash : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        val getsavecheckID = AppUtils.getsavecheckID(applicationContext)
//        val getsaveloginID = AppUtils.getsaveloginID(applicationContext)
//        if (getsavecheckID != "") {
//            startActivity(Intent(this, MainActivity::class.java))
//        } else {
            Handler().postDelayed(
                {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, SPLASH_TIME_OUT
            )
//        }
    }
}