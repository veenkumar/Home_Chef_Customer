package com.veen.homechef.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Model.Login.LoginRequest
import com.veen.homechef.Model.Login.LoginResponse
import com.veen.homechef.Model.Login.Relogin.ReloginReq
import com.veen.homechef.Model.Login.Relogin.ReloginRes
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class Login : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var signup: TextView? = null
    private var loginbtn: Button? = null
    private lateinit var loginemail: EditText
    private lateinit var loginpass: EditText
    private var forgotpass: TextView? = null
    private lateinit var loginButton: LoginButton
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        loginemail = findViewById(R.id.login_email_id)
        loginpass = findViewById(R.id.login_pass)
        loginbtn = findViewById(R.id.loginbutton)
        forgotpass = findViewById(R.id.login_forgot_pass)
        signup = findViewById(R.id.login_getstarted)

        getHashkey()
        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()
        val EMAIL = "email"
        callbackManager = CallbackManager.Factory.create()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCancel() {
                        // App code
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                    }
                })

        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setOnClickListener {
            loginButton.setReadPermissions(Arrays.asList(EMAIL))
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        }
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

        sign_in_btn.setOnClickListener {
            signIn()
        }

        forgotpass!!.setOnClickListener {
            startActivity(Intent(applicationContext, ForgotPass::class.java))
        }

        signup!!.setOnClickListener {
            startActivity(Intent(applicationContext, Signup::class.java))
        }

//        loginemail.setText("bk399aaa@gmail.com")
//        loginpass.setText("123")

        loginbtn!!.setOnClickListener {
            if (TextUtils.isEmpty(loginemail.text.toString())) {
                loginemail.setError("field can't be empty")
            } else if (TextUtils.isEmpty(loginpass.text.toString())) {
                loginpass.setError("field can't be empty")
            } else {
                login()
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun getHashkey() {
        try {
            val info = packageManager.getPackageInfo(
                    applicationContext.packageName,
                    PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i("Base64", Base64.encodeToString(md.digest(), Base64.NO_WRAP))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("Name not found", e.message, e)
        } catch (e: NoSuchAlgorithmException) {
            Log.d("Error", e.message, e)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

//        var accessToken = AccessToken.getCurrentAccessToken()
//        var isLoggedIn = accessToken != null && !accessToken!!.isExpired

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
//                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }
        }
    }


    private fun login() {
        try {
            loginrefresh.isRefreshing = true
            RetrofitInstance.instence?.login(
                    LoginRequest(
                            loginemail.text.toString(),
                            loginpass.text.toString()
                    )
            )!!
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()!!.status == true) {
                                val LoginID = response.body()!!.data.id
                                val token = response.body()!!.token

                                AppUtils.saveloginID(applicationContext, LoginID)
                                AppUtils.savetoken(applicationContext, token)

                                Toast.makeText(
                                        applicationContext,
                                        "Login Successful",
                                        Toast.LENGTH_SHORT
                                )
                                        .show()
                                afterlogin(LoginID, token)
                                startActivity(Intent(this@Login, MainActivity::class.java))
                                finish()
                                loginrefresh.isRefreshing = false
                            }
                        } else {
                            Toast.makeText(
                                    applicationContext,
                                    "Invalid email or password!",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("TAG", "onFailure: Failed")
                        loginrefresh.isRefreshing = false
                    }
                })
            loginrefresh.isRefreshing = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun afterlogin(loginID: String, token: String) {
        try {
            val getsavePhoneUnique = AppUtils.getsavePhoneUnique(applicationContext)
            RetrofitInstance.instence?.relogin(token, ReloginReq(
                getsavePhoneUnique,
                loginID.toInt()
            ))!!.enqueue(object : Callback<ReloginRes> {
                override fun onResponse(call: Call<ReloginRes>, response: Response<ReloginRes>) {
                    if (response.isSuccessful) {
                        Log.d("TAG", "onResponse: Success")
                    }
                }

                override fun onFailure(call: Call<ReloginRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    /*val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()*/
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }
}
