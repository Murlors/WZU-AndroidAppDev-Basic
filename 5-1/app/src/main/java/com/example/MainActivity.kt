package com.example.hlt

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private var countingThread: Thread? = null
    private var count = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tv_result)
        btnStart = findViewById(R.id.btn_start)
        btnStop = findViewById(R.id.btn_stop)

        btnStart.setOnClickListener {
            count = 0f
            if (countingThread == null) {
                countingThread = Thread {
                    while (!Thread.currentThread().isInterrupted) {
                        try {
                            Thread.sleep(10)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                            break
                        }
                        count+=0.01f
                        runOnUiThread {
                            tvResult.text = "%.2f".format(count)
                        }
                    }
                }
                countingThread!!.start()
                btnStart.isEnabled = false
                btnStop.isEnabled = true
            }
        }

        btnStop.setOnClickListener {
            if (countingThread != null) {
                countingThread!!.interrupt()
                countingThread = null
                btnStart.isEnabled = true
                btnStop.isEnabled = false
            }
        }
    }
}