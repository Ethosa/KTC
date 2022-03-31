package com.ethosa.ktc.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Permissions {
    fun hasPermissions(context: Context?): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                Environment.isExternalStorageManager()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
            }
            else -> {
                true
            }
        }
    }

    fun requestPermissions(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", activity.packageName))
                activity.startActivityForResult(intent, requestCode)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                activity.startActivityForResult(intent, requestCode)
            }
        } else {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCode
            )
        }
    }
}