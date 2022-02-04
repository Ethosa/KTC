package com.ethosa.ktc.college

import android.util.Log
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Call
import okhttp3.Response
import okhttp3.Request
import java.io.IOException

class CollegeApi : Callback{
    private var client: OkHttpClient = OkHttpClient()
    private var _callback: CollegeCallback<Response>? = null

    /**
     * Gets the last news and receives it in onResponse method
     */
    fun lastNews(callback: CollegeCallback<Response>? = null) {
        val request: Request.Builder = Request.Builder()
            .get()
            .url("http://api.kansk-tc.ru/news/")

        _callback = callback
        client.newCall(request.build()).enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {
        Log.e("CollegeAPI", e.message.toString())
    }

    override fun onResponse(call: Call, response: Response) {
        _callback?.onResponse(response)
        _callback = null
    }
}