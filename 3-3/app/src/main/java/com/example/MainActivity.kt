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

        val dimens = resources.obtainTypedArray(R.array.myDimens)
        val dimenNames = resources.getStringArray(R.array.myDimenNames)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dimenNames)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tv.textSize = dimens.getDimension(position, 10f)
                //利用Logcat打印字号值对应的真实值
                println("字号值：${dimens.getDimension(position, 10f)}")
            }
        }

    }
}