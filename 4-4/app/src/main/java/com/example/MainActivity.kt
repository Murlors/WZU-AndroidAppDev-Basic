package com.example.hlt

import android.app.Dialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
    private lateinit var messageButton: Button
    private lateinit var listButton: Button
    private lateinit var radioButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tv_result)
        messageButton = findViewById(R.id.btn_message)
        listButton = findViewById(R.id.btn_list)
        radioButton = findViewById(R.id.btn_radio)

        val items = arrayOf("游泳", "跑步", "篮球")
        var selectedItem = 0

        messageButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("提示")
            dialog.setMessage("这是一个消息提示框")
            dialog.setPositiveButton("退出", null)
            dialog.show()
        }

        listButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("列表框")
            dialog.setItems(items) { _, which ->
                selectedItem = which
                tvResult.text = items[which]
            }
            dialog.setNegativeButton("退出", null)
            dialog.show()
        }

        radioButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("单选对话框")

            dialog.setSingleChoiceItems(items, selectedItem) { _, which ->
                selectedItem = which
            }
            dialog.setPositiveButton("确定") { _, _ ->
                tvResult.text = items[selectedItem]
            }
            dialog.setNegativeButton("退出", null)
            dialog.show()
        }
    }
}