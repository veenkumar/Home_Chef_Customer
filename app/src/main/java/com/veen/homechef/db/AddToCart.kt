package com.veen.homechef.db

import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class AddToCart(
    val image: String,
    val name: String,
    val discription: String,
    val amount: String,
    val size: String
):Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
/*    override fun getActionId(): Int {
        var id: Int = 0

        return actionId
    }

    override fun getArguments(): Bundle {
        return arguments
    }*/

    /* val savedcart = CartDatabase(requireContext()).getNoteDao().getalldate()
                   recyclerView!!.layoutManager =
                           LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                   adapter = AddToCartAdapter(requireContext(), savedcart)
                   recyclerView!!.adapter = adapter*/
}