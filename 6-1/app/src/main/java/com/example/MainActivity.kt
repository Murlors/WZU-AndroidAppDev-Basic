package com.example.hlt

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var textName: EditText
    private lateinit var textPwd: EditText
    private lateinit var checkBoxSave: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textName = findViewById(R.id.text_name)
        textPwd = findViewById(R.id.text_password)
        checkBoxSave = findViewById(R.id.checkBox_save)
        val isSave: Boolean = SharedUtils.loadCheckStatus(this)
        checkBoxSave.setChecked(isSave)
        if (isSave) {
            val name: String? = SharedUtils.loadName(this)
            val pwd: String? = SharedUtils.loadPassword(this)
            textName.setText(name)
            textPwd.setText(pwd)
        }
        findViewById<View>(R.id.button_login).setOnClickListener {
            saveData()
            showToast("pwd=" + textPwd.getText().toString())
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }

    private fun saveData() {
        if (checkBoxSave.isChecked) {
            SharedUtils.saveName(this, textName.getText().toString())
            SharedUtils.savePassword(this, textPwd.getText().toString())
            SharedUtils.saveCheckStatus(this, true)
        } else {
            SharedUtils.saveName(this, "")
            SharedUtils.savePassword(this, "")
            SharedUtils.saveCheckStatus(this, false)
        }
    }
}