package com.example.hlt

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var buttonRed: Button
    private lateinit var buttonBlue: Button
    private lateinit var buttonOther: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.text_view)
        buttonRed = findViewById(R.id.button_red)
        buttonBlue = findViewById(R.id.button_blue)
        buttonOther = findViewById(R.id.button_other)

        buttonRed.setOnClickListener {
            textView.setTextColor(Color.RED)
        }
        buttonBlue.setOnClickListener {
            textView.setTextColor(Color.BLUE)
        }
        buttonOther.setOnClickListener {
            val otherColor = resources.getColor(R.color.other_color, null)
            textView.setTextColor(otherColor)
        }
    }
}