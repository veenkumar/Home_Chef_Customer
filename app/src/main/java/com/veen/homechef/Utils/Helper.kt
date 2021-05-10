package com.veen.homechef.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URISyntaxException
import java.util.*

class Helper {
    companion object {
        fun isNetworkAvailable(activity: Activity): Boolean {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }

        //Getting the permission status: Storage
        fun isReadExternalStoragePermissionAllowed(activity: Activity): Boolean {
            val result = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun isCameraPermission(activity: Activity): Boolean {
            val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun prepareFilePart(partName: String, fileUri: Uri, context: Context): MultipartBody.Part {
            var file: File? = null
            try {
                file = File(PathUtil.getPath(context, fileUri))
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            val requestFile = RequestBody.create(
                Objects.requireNonNull(context.contentResolver.getType(fileUri)!!)
                    .toMediaTypeOrNull(), Objects.requireNonNull<File>(file)
            )
            return MultipartBody.Part.createFormData(partName, file!!.name, requestFile)
        }
    }
}