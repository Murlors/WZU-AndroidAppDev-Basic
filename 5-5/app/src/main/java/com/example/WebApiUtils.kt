package com.example.hlt

import android.app.Activity
import com.google.gson.Gson
import okhttp3.*
import okio.IOException

object WebApiUtils {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun fetchDataAsync(activity: Activity, url: String, l: OnFetchFinishListener) {
        val request = Request.Builder().url(url).get().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity.runOnUiThread {
                    l.onFetchFailed(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val result = gson.fromJson(body, Array<Locate>::class.java)

                activity.runOnUiThread {
                    l.onFetchFinish(result)
                    println(result.map { it })
                }
            }
        })
    }

    interface OnFetchFinishListener {
        fun onFetchFinish(result: Array<Locate>) {}
        fun onFetchFailed(e: Exception) {}
    }
}