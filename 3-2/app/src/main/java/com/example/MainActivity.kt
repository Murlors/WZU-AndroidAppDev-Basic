package com.example.hlt

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tv: TextView
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)
        spinner = findViewById(R.id.spinner)

        val colors = resources.getIntArray(R.array.myColors)
        val colorNames = resources.getStringArray(R.array.myColorNames)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colorNames)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tv.setTextColor(colors[position])
            }
        }
    }
}