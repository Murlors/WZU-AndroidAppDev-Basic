package com.example.hlt

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tv: TextView
    private lateinit var imageView: ImageView
    private lateinit var listView: ListView

    val picIds = arrayOf(R.drawable.hangzhou,R.drawable.ningbo,R.drawable.wenzhou)
    val picNames = arrayOf("杭州","宁波","温州")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)
        imageView = findViewById(R.id.imageView)
        listView = findViewById(R.id.listView)

        listView.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, picNames)

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            imageView.setImageResource(picIds[position])
        }

    }
}