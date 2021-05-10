package com.veen.homechef.Activity.profile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Model.Profile.ChangePass.ChangePassRequest
import com.veen.homechef.Model.Profile.ChangePass.ChangePassResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class ChangePassword : AppCompatActivity() {
    private lateinit var oldpass: EditText
    private lateinit var newpass: EditText
    private lateinit var confirm: EditText
    private var changepass: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_change_password)

        try {
            oldpass = findViewById(R.id.change_pass_oldpass)
            newpass = findViewById(R.id.change_pass_newpass)
            confirm = findViewById(R.id.change_pass_confirm)
            changepass = findViewById(R.id.chnage_pass_btn)
            var regexPassword =
                Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}")

            changepass!!.setOnClickListener {

                if (TextUtils.isEmpty(oldpass.text.toString())) {
                    oldpass.setError("Please Enter Old Password")
                } else if (TextUtils.isEmpty(newpass.text.toString())) {
                    newpass.setError("Please Enter New Password")
                } else if (TextUtils.isEmpty(confirm.text.toString())) {
                    confirm.setError("Please Enter Confirm Password")
                } else if (newpass.text.toString().length < 8 || regexPassword.matcher(newpass.text.toString())
                        .find()
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Password length should be at least 8 character long.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (newpass.text.toString() == confirm.text.toString()) {
                        changePassword()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "New & Confirm Password Not Matched",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changePassword() {
        try {
            val getloginID = AppUtils.getsaveloginID(applicationContext).toInt()
            val gettoken = AppUtils.getsavetoken(applicationContext)
            RetrofitInstance.instence?.changepass(
                gettoken, ChangePassRequest(
                    newpass.text.toString(),
                    oldpass.text.toString(),
                    getloginID
                )
            )!!.enqueue(object : Callback<ChangePassResponse> {
                override fun onResponse(
                    call: Call<ChangePassResponse>,
                    response: Response<ChangePassResponse>
                ) {
                    if (response.body()!!.status == true) {
                        if (response.isSuccessful) {
                            newpass.text.clear()
                            confirm.text.clear()
                            oldpass.text.clear()
                            Toast.makeText(
                                applicationContext,
                                "Password Change Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(applicationContext, ChangePassword::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "${response.body()!!.msg}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ChangePassResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}