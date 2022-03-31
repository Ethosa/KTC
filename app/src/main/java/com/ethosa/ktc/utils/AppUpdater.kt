package com.ethosa.ktc.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.ethosa.ktc.college.CollegeCallback
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File


class AppUpdater(
    private val context: Context
) {
    fun update(url: String) {
        val path = Environment.getDownloadCacheDirectory().toString() + "/KTC/"
        val req = Request.Builder()
            .url(url)
            .get()
            .build()
        OkHttpClient().newCall(req).enqueue(object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                val file = File(path)
                file.mkdirs()
                val apkFile = File(file, "actual_app.apk")
                val output = apkFile.outputStream()
                val input = response.body?.byteStream()
                val buffer = ByteArray(1024)
                var len1: Int
                while (input!!.read(buffer).also { len1 = it } != -1) {
                    output.write(buffer, 0, len1)
                }
                output.close()
                input.close()

                val install = Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(path + "app.apk"))
                    .setType("application/android.com.app")
                context.startActivity(install)
            }
        })
    }
}