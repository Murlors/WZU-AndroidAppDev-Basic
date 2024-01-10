package com.example.hlt

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tv: TextView
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)
        gridView = findViewById(R.id.grid_view)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            (0..99).map { String.format("Item%02d", it) })
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, i, _ ->
            tv.text = "Item%02d".format(i)
        }
    }
}