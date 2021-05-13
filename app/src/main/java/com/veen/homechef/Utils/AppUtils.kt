package com.veen.homechef.Utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

object AppUtils {
    fun saveloginID(context: Context?, loginID: String) {
        val shred = context?.getSharedPreferences("Login", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("Login", loginID)?.apply()
    }

    fun getsaveloginID(context: Context?): String {
        val shred = context?.getSharedPreferences("Login", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("Login", "") ?: ""
    }

    fun savetoken(context: Context?, token: String) {
        val shred = context?.getSharedPreferences("token", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("token", token)?.apply()
    }

    fun getsavetoken(context: Context?): String {
        val shred = context?.getSharedPreferences("token", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("token", "") ?: ""
    }

    fun savechefID(context: Context?, getchefID: String) {
        val shred = context?.getSharedPreferences("chefID", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("chefID", getchefID)?.apply()
    }

    fun getsavechefID(context: Context?): String {
        val shred = context?.getSharedPreferences("chefID", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("chefID", "") ?: ""
    }

    fun saveorderID(context: Context?, orderID: String) {
        val shred = context?.getSharedPreferences("order", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("order", orderID)?.apply()
    }

    fun getsaveorderID(context: Context?): String {
        val shred = context?.getSharedPreferences("order", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("order", "") ?: ""
    }

    fun savePhoneUnique(context: Context?, phoneuniqueID: String?) {
        val shred = context?.getSharedPreferences("Unique", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("Unique", phoneuniqueID)?.apply()
    }

    fun getsavePhoneUnique(context: Context?): String {
        val shred = context?.getSharedPreferences("Unique", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("Unique", "") ?: ""
    }

}