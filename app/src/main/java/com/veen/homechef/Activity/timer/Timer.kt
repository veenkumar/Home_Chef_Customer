package com.veen.homechef.Activity.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Activity.MainActivity
import com.veen.homechef.Activity.TOPIC
import com.veen.homechef.Activity.profile.MyOrder
import com.veen.homechef.Model.cancelorder.CancelOrderReq
import com.veen.homechef.Model.cancelorder.CancelOrderRes
import com.veen.homechef.R
import com.veen.homechef.Retrofit.Retrofitfirebase
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.firebase.FirebaseService
import com.veen.homechef.firebase.NotificationData
import com.veen.homechef.firebase.PushNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

const val TOPIC = "/topics/myTopic2"

class Timer : AppCompatActivity() {
    private var mTextViewCountDown: TextView? = null
    private var mTextorder: TextView? = null
    private var mButtonStartPause: Button? = null
    private var mButtonReset: Button? = null
    private var mCountDownTimer: CountDownTimer? = null
    private var mTimerRunning = false
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var recipientToken: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        try {
            mTextViewCountDown = findViewById(R.id.text_view_countdown)
            mButtonStartPause = findViewById(R.id.button_start_pause)
            mButtonReset = findViewById(R.id.button_reset)
            mTextorder = findViewById(R.id.order)

            val getsaveorderID = AppUtils.getsaveorderID(applicationContext)

            mTextorder!!.setText("Order ID: #" + getsaveorderID)

            FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                FirebaseService.token = it.token
                recipientToken = it.token
//                etToken.setText(it.token)
            }
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
//        mButtonStartPause!!.setOnClickListener(View.OnClickListener {
//            if (mTimerRunning) {
//                pauseTimer()
//            } else {
//                startTimer()
//            }
//        })
//        mButtonReset!!.setOnClickListener(View.OnClickListener { resetTimer() })
            startTimer()
            updateCountDownText()

            mButtonStartPause!!.setOnClickListener {
                val title = "Home Chef"
                val message = "Your Order Request cancel!"
//                    val recipientToken = etToken.text.toString()
                if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                    PushNotification(
                        NotificationData(title, message),
                        recipientToken
                    ).also {
                        sendNotification(it)
                    }
                }
                var gettoken = AppUtils.getsavetoken(applicationContext)
                val getsaveorderID = AppUtils.getsaveorderID(applicationContext)

                RetrofitInstance.instence?.cancelorder(gettoken, CancelOrderReq(getsaveorderID))!!.enqueue(
                    object :
                        Callback<CancelOrderRes> {
                        override fun onResponse(
                            call: Call<CancelOrderRes>,
                            response: Response<CancelOrderRes>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()!!.status == true) {
                                    startActivity(Intent(applicationContext, MyOrder::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Order Id Invalid.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<CancelOrderRes>, t: Throwable) {
                            Log.d("TAG", "onFailure: Failed")
                        }
                    })

            }
        } catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun startTimer() {
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimerRunning = false
//                mButtonStartPause!!.text = "Start"
//                mButtonStartPause!!.visibility = View.INVISIBLE
//                mButtonReset!!.visibility = View.VISIBLE.
                startActivity(Intent(applicationContext, MyOrder::class.java))
                finish()
            }
        }.start()
        mTimerRunning = true
//        mButtonStartPause!!.text = "pause"
        mButtonStartPause!!.text = "Order Cancel"
        mButtonReset!!.visibility = View.INVISIBLE
    }

    private fun pauseTimer() {
        mCountDownTimer!!.cancel()
        mTimerRunning = false
        mButtonStartPause!!.text = "Start"
        mButtonReset!!.visibility = View.VISIBLE
    }

    private fun resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS
        updateCountDownText()
        mButtonReset!!.visibility = View.INVISIBLE
        mButtonStartPause!!.visibility = View.VISIBLE
    }

    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        mTextViewCountDown!!.text = timeLeftFormatted
    }

    companion object {
        private const val START_TIME_IN_MILLIS: Long = 60000
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = Retrofitfirebase.api.postNotification(notification)
            if(response.isSuccessful) {
//                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody().toString())
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }
}