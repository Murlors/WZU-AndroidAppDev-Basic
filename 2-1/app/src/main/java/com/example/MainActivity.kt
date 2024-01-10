package com.example.hlt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var tvResult: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvResult = findViewById(R.id.tv_result)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            val randomNumber = (0..999).random()
            tvResult.text = randomNumber.toString()
            val toastMessage = "第${++count}次按钮"
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }
}