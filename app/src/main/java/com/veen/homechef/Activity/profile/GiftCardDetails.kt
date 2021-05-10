package com.veen.homechef.Activity.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils

class GiftCardDetails : AppCompatActivity() {
    private var gimage: ImageView? = null
    private var gtype: TextView? = null
    private var gdescription: TextView? = null
    private var gdiscount: TextView? = null
    private var gamount: TextView? = null
    private var gpurchase: TextView? = null
    private var gcardexpired: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift_card)

         gimage = findViewById(R.id.gift_image)
         gtype = findViewById(R.id.gift_type1)
         gdescription = findViewById(R.id.gift_description)
         gdiscount = findViewById(R.id.gift_discount1)
         gamount = findViewById(R.id.gift_amount1)
         gpurchase = findViewById(R.id.gift_purchased1)
         gcardexpired = findViewById(R.id.gift_expired1)

        val getloginID = AppUtils.getsaveloginID(applicationContext).toInt()
        val gettoken = AppUtils.getsavetoken(applicationContext)

//        RetrofitInstance.instence?.giftcards(gettoken, GiftCardDetailsRequest(
//            7
//        ))!!.enqueue(object : Callback<GiftCardDetailsResponse> {
//            override fun onResponse(call: Call<GiftCardDetailsResponse>, response: Response<GiftCardDetailsResponse>) {
//                if (response.isSuccessful) {
//                    Picasso.get().load(response.body()!!.data.image).into(gimage)
//                    gtype?.setText(response.body()!!.data.title)
//                    gdescription?.setText(HtmlCompat.fromHtml(response.body()!!.data.description, HtmlCompat.FROM_HTML_MODE_LEGACY))
//                    gdiscount?.setText(response.body()!!.data.amount)
//                    gamount?.setText(response.body()!!.data.minimum_price_range)
////                    gtype?.setText(response.body()!!.data.title)
////                    gtype?.setText(response.body()!!.data.title)
//
//                }
//            }
//
//            override fun onFailure(call: Call<GiftCardDetailsResponse>, t: Throwable) {
////                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
//            }
//        })

    }
}