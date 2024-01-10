package com.example.hlt

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import okio.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var btnAsync: Button
    private lateinit var btnBlock: Button
    private lateinit var listView: ListView

    private val client = OkHttpClient()
    private val gson = Gson()

    data class City(val name: String, val id: String) {
        override fun toString(): String {
            return "$name,id=$id"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edit_text)
        btnAsync = findViewById(R.id.btn_async)
        btnBlock = findViewById(R.id.btn_block)
        listView = findViewById(R.id.list_view)


        btnAsync.setOnClickListener { fetchDataAsync() }
        btnBlock.setOnClickListener { fetchDataBlock() }
    }

    private fun fetchDataAsync() {
        val url = editText.text.toString()
        val request = Request.Builder().url(url).get().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val result = gson.fromJson(body, Array<City>::class.java)

                runOnUiThread {
                    listView.adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        result
                    )
                }
            }
        })
    }

    private fun fetchDataBlock() {
        val url = editText.text.toString()
        val request = Request.Builder().url(url).get().build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                val body = response.body?.string()
                val result = gson.fromJson(body, Array<City>::class.java)

                runOnUiThread {
                    listView.adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        result
                    )
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }.start()
    }
}