package com.example.hlt

import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var checkBoxClimb: CheckBox
    private lateinit var checkBoxRun: CheckBox
    private lateinit var checkBoxSwim: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text_view)
        checkBoxClimb = findViewById(R.id.checkBox_climb)
        checkBoxRun = findViewById(R.id.checkBox_run)
        checkBoxSwim = findViewById(R.id.checkBox_swim)

        val checkBoxes = listOf(checkBoxClimb, checkBoxRun, checkBoxSwim)

        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                updateCheckBox()
            }
        }
    }

    private fun updateCheckBox() {
        val selectedSports = mutableListOf<String>()

        if (checkBoxClimb.isChecked) {
            selectedSports.add(checkBoxClimb.text.toString())
        }
        if (checkBoxRun.isChecked) {
            selectedSports.add(checkBoxRun.text.toString())
        }
        if (checkBoxSwim.isChecked) {
            selectedSports.add(checkBoxSwim.text.toString())
        }

        val text = selectedSports.joinToString(" ")
        textView.text = text
    }
}