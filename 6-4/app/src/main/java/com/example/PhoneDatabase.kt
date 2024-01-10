package com.example.hlt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorWrapper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils


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
        return PinyinUtils.generateLookup(name)
    }

    fun fuzzyQuery(match: String): Cursor {
        if (TextUtils.isEmpty(match)) {
            return queryAll()
        }
        val sql = java.lang.String.format(
            "select * from %s where %s like ? or %s like ?",
            TABLE_NAME, KEY_LOOK_UP, KEY_PHONE
        )
        val args = arrayOf("%$match%", "%$match%")
        return db.rawQuery(sql, args)
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
    fun updateData(name: String, phone: String, id: Long): Int {
        val cv = enCodeContentValues(name, phone)
        return db.update(TABLE_NAME, cv, "_id=$id", null)
    }

    fun deleteData(id: Long): Int {
        return db.delete(TABLE_NAME, "_id=$id", null)
    }

    fun queryById(id: Long): PhoneCursor {
        val sql = "select * from $TABLE_NAME where _id=$id"
        val c = db.rawQuery(sql, null)
        return PhoneCursor(c)
    }
    internal inner class DatabaseHelper : SQLiteOpenHelper(context, DB_NAME, null, version) {
        override fun onCreate(db: SQLiteDatabase) {
            val sql = "create table $TABLE_NAME (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " $KEY_NAME text, $KEY_PHONE text, $KEY_LOOK_UP text)"
            db.execSQL(sql)
            val cv1 = enCodeContentValues("重庆", "023")
            val cv2 = enCodeContentValues("长沙", "0731")
            val cv3 = enCodeContentValues("旅顺", "0411")
            db.insert(TABLE_NAME, null, cv1)
            db.insert(TABLE_NAME, null, cv2)
            db.insert(TABLE_NAME, null, cv3)
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

    class PhoneCursor(var c: Cursor) : CursorWrapper(c) {
        val name: String
            get() {
                val idx = c.getColumnIndex(KEY_NAME)
                return c.getString(idx)
            }
        val phone: String
            get() {
                val idx = c.getColumnIndex(KEY_PHONE)
                return c.getString(idx)
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
