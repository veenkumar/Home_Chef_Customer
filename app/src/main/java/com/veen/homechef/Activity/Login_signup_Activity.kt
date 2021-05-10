package com.veen.homechef.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.veen.homechef.R

class Login_signup_Activity : AppCompatActivity(), View.OnClickListener {
    private var visitlogin: Button? = null
    private var visitsignup: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        var token = getSharedPreferences("", Context.MODE_PRIVATE)

        if (token.getString("loginusername","")!=""){
            startActivity(Intent(this, MainActivity::class.java))
        }

        visitlogin = findViewById(R.id.login_login_btn)
        visitsignup = findViewById(R.id.sign_sign_btn)

        visitlogin?.setOnClickListener(this)
        visitsignup?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.login_login_btn -> {
                startActivity(Intent(applicationContext, Login::class.java))
            }
            R.id.sign_sign_btn -> {
                startActivity(Intent(applicationContext, Signup::class.java))
            }
        }
    }
}