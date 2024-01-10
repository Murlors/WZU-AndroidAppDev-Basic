package com.example.hlt

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText


class PhoneDialog(private val context: Context, private val title: String) {
    interface OnSubmitListener {
        fun onSubmit(updatedName: String, updatedPhone: String)
    }

    fun showDialog(name: String, phone: String, l: OnSubmitListener) {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.dialog_view, null, false)
        val et_name = v.findViewById<EditText>(R.id.dialog_et_name)
        val et_phone = v.findViewById<EditText>(R.id.dialog_et_phone)
        et_name.setText(name)
        et_phone.setText(phone)
        val bl = AlertDialog.Builder(context)
        bl.setTitle(title)
        bl.setView(v)
        bl.setNegativeButton("取消", null)
        bl.setPositiveButton("确定") { dialog, which ->
            l.onSubmit(
                et_name.text.toString(),
                et_phone.text.toString()
            )
        }
        bl.show()
    }
}

