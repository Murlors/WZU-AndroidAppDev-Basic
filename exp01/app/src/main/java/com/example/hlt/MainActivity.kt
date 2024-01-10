package com.example.hlt

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var buttonGetName: Button
    private lateinit var textViewName: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var checkBoxRed: CheckBox
    private lateinit var checkBoxGreen: CheckBox
    private lateinit var checkBoxBlue: CheckBox
    private lateinit var textViewRadio: TextView
    private lateinit var textViewCheckBox: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        editTextName = findViewById(R.id.edit_text_name)
        buttonGetName = findViewById(R.id.button_get_name)
        textViewName = findViewById(R.id.text_view_name)
        radioGroup = findViewById(R.id.radio_group)
        checkBoxRed = findViewById(R.id.check_box_red)
        checkBoxGreen = findViewById(R.id.check_box_green)
        checkBoxBlue = findViewById(R.id.check_box_blue)
        textViewRadio = findViewById(R.id.text_view_radio)
        textViewCheckBox = findViewById(R.id.text_view_check_box)
    }

    private fun setListeners() {
        buttonGetName.setOnClickListener {
            textViewName.text = editTextName.text
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            textViewRadio.text = radioButton.text
        }

        val checkBoxes = listOf(checkBoxRed, checkBoxGreen, checkBoxBlue)
        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                val selectedColors = checkBoxes.filter { it.isChecked }.map { it.text.toString() }
                textViewCheckBox.text = selectedColors.joinToString(" ")
            }
        }
    }
}