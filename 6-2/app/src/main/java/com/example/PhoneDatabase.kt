package com.example.hlt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PhoneDatabase(private val context: Context) {
    private val version = 1
    private val databaseHelper: DatabaseHelper = DatabaseHelper()
    private lateinit var db: SQLiteDatabase

    fun open() {
        if (!this::db.isInitialized) {
            db = databaseHelper.writableDatabase
        }
    }

    fun close() {
        if (this::db.isInitialized) {
            db.close()
        }
    }

    private fun enCodeContentValues(name: String, phone: String): ContentValues {
        val cv = ContentValues()
        cv.put(KEY_NAME, name)
        cv.put(KEY_PHONE, phone)
        cv.put(KEY_LOOK_UP, generateLookup(name))
        return cv
    }

    private fun generateLookup(name: String): String {
        return ""
    }

    fun queryAll(): Cursor {
        val sql = "select * from $TABLE_NAME"
        val c = db.rawQuery(sql, null)
        return c
    }

    fun insertData(name: String, phone: String): Long {
        val cv = enCodeContentValues(name, phone)
        return db.insert(TABLE_NAME, null, cv)
    }

    fun reset() {
        databaseHelper.reset(db)
    }

    internal inner class DatabaseHelper : SQLiteOpenHelper(context, DB_NAME, null, version) {
        override fun onCreate(db: SQLiteDatabase) {
            val sql = "create table $TABLE_NAME (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " $KEY_NAME text, $KEY_PHONE text, $KEY_LOOK_UP text)"
            db.execSQL(sql)
            val cv1 = enCodeContentValues("张三", "1234")
            db.insert(TABLE_NAME, null, cv1)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            reset(db)
        }

        fun reset(db: SQLiteDatabase) {
            val sql = "drop table if exists $TABLE_NAME"
            db.execSQL(sql)
            onCreate(db)
        }
    }

    companion object {
        private const val DB_NAME = "phone.db"
        const val TABLE_NAME: String = "contact"
        const val KEY_NAME: String = "name"
        const val KEY_PHONE: String = "phone"
        const val KEY_LOOK_UP: String = "look_up"
    }
}
