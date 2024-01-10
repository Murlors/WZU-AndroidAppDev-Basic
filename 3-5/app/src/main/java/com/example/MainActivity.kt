package com.example.hlt

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    companion object {
        const val KEY_NAME = "key_name"
        const val KEY_PHONE = "key_phone"
        const val KEY_PIC_ID = "key_pic_id"
    }

    private lateinit var tv: TextView
    private lateinit var listView: ListView

    private val cityNames = arrayOf("杭州", "宁波", "温州")
    private val cityImages = arrayOf(R.drawable.hangzhou, R.drawable.ningbo, R.drawable.wenzhou)
    private val cityPhones = arrayOf("0571-12345678", "0574-12345678", "0577-12345678")

    private var cityList = ArrayList<HashMap<String,Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)
        listView = findViewById(R.id.listView)

        for (i in cityNames.indices) {
            val map = HashMap<String, Any>()
            map[KEY_NAME] = cityNames[i]
            map[KEY_PIC_ID] = cityImages[i]
            map[KEY_PHONE] = cityPhones[i]
            cityList.add(map)
        }

        val simpleAdapter = SimpleAdapter(this, cityList, R.layout.row_view, arrayOf(KEY_NAME, KEY_PIC_ID, KEY_PHONE), intArrayOf(R.id.row_view_tv_name,R.id.row_view_iv,R.id.row_view_tv_phone))
        listView.adapter = simpleAdapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val name = cityList[position][KEY_NAME].toString()
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
        }
    }
}

