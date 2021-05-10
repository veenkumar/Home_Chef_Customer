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

    fun saveemailID(context: Context?, emailID: String) {
        val shred = context?.getSharedPreferences("Email", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("Email", emailID)?.apply()
    }

    fun getsaveemailID(context: Context?): String {
        val shred = context?.getSharedPreferences("Email", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("Email", "") ?: ""
    }

    fun saveImageID(context: Context?, imageID: String) {
        val shred = context?.getSharedPreferences("Image", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("Image", imageID)?.apply()
    }

    fun getsaveImageID(context: Context?): String {
        val shred = context?.getSharedPreferences("Image", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("Image", "") ?: ""
    }

    fun savenameID(context: Context?, nameID: String) {
        val shred = context?.getSharedPreferences("Name", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("Name", nameID)?.apply()
    }

    fun getsavenameID(context: Context?): String {
        val shred = context?.getSharedPreferences("Name", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("Name", "") ?: ""
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

    fun savecheckID(context: Context?, remembermeID: String) {
        val shred = context?.getSharedPreferences("Checkbox", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("Checkbox", remembermeID)?.apply()
    }

    fun getsavecheckID(context: Context?): String {
        val shred = context?.getSharedPreferences("Checkbox", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("Checkbox", "") ?: ""
    }

    fun saveorderID(context: Context?, orderID: String) {
        val shred = context?.getSharedPreferences("order", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("order", orderID)?.apply()
    }

    fun getsaveorderID(context: Context?): String {
        val shred = context?.getSharedPreferences("order", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("order", "") ?: ""
    }

    fun deleteloginID(context: Context?) {
        val shred = context?.getSharedPreferences("Checkbox", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.remove("Checkbox")?.apply()
//        shred?.edit()?.clear()?.apply()
    }

}