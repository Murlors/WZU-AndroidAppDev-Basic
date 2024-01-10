package com.example.hlt

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


object SharedUtils {
    private const val KEY_NAME = "key_name"
    private const val KEY_PWD = "key_pwd"
    private const val KEY_CHECK = "key_check"

    private fun getShared(activity: Activity): SharedPreferences {
        return activity.getSharedPreferences(
            activity.getLocalClassName(),
            Context.MODE_PRIVATE
        )
    }

    fun saveName(activity: Activity, name: String?): Boolean {
        val shared = getShared(activity)
        val edit = shared.edit()
        edit.putString(KEY_NAME, name)
        return edit.commit()
    }

    fun savePassword(activity: Activity, pwd: String?): Boolean {
        val shared = getShared(activity)
        val edit = shared.edit()
        edit.putString(KEY_PWD, pwd)
        return edit.commit()
    }

    fun saveCheckStatus(activity: Activity, isChecked: Boolean): Boolean {
        val shared = getShared(activity)
        val edit = shared.edit()
        edit.putBoolean(KEY_CHECK, isChecked)
        return edit.commit()
    }

    fun loadName(activity: Activity): String? {
        val shared = getShared(activity)
        return shared.getString(KEY_NAME, "")
    }

    fun loadPassword(activity: Activity): String? {
        val shared = getShared(activity)
        return shared.getString(KEY_PWD, "")
    }

    fun loadCheckStatus(activity: Activity): Boolean {
        val shared = getShared(activity)
        return shared.getBoolean(KEY_CHECK, false)
    }
}


