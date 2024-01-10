package com.example.hlt

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
    private lateinit var checkBoxButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tv_result)
        checkBoxButton = findViewById(R.id.btn_check_box)

        val items = arrayOf("游泳", "跑步", "篮球")
        var savedCheckedItems = booleanArrayOf(false, false, false)

        checkBoxButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("多选对话框")
            val checkedItems = savedCheckedItems.clone()
            builder.setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            builder.setPositiveButton("确定") { _, _ ->
                savedCheckedItems = checkedItems
                tvResult.text = items.filterIndexed { index, _ ->
                    savedCheckedItems[index]
                }.joinToString(" ")
            }
            builder.setNegativeButton("取消", null)
            builder.show()
        }
    }
}