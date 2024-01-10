package com.example.hlt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorWrapper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*


class PhoneDatabase(private val context: Context) {
    private val version = 1
    private val databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase

    init {
        databaseHelper = DatabaseHelper()
    }

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

    fun queryAll(): Cursor {
        val sql = String.format("select * from %s", TABLE_NAME)
        val cursor = db.rawQuery(sql, null)
        return cursor
    }

    fun fuzzyQuery(match: String): Cursor {
        if (TextUtils.isEmpty(match)) {
            return queryAll()
        }
        val sql = "select * from $TABLE_NAME where $KEY_LOOK_UP like ? or $KEY_CONTENT like ?"
        val args = arrayOf("%$match%", "%$match%")
        val c = db.rawQuery(sql, args)
        return c
    }

    fun queryById(id: Long): PhoneCursor {
        val sql = "select * from $TABLE_NAME where _id=$id"
        val c = db.rawQuery(sql, null)
        val phoneCursor = PhoneCursor(c)
        phoneCursor.moveToFirst()
        return phoneCursor
    }

    fun insertData(title: String, content: String, imagePath: String): Long {
        val cv = encodeContentValues(title, content, imagePath)
        val s = cv[KEY_MODIFY_TIME] as String
        cv.put(KEY_CREATE_TIME, s)
        return db.insert(TABLE_NAME, null, cv)
    }

    fun updateData(title: String, content: String, id: Long, imagePath: String): Long {
        val cv = encodeContentValues(title, content, imagePath)
        return db.update(TABLE_NAME, cv, "_id=$id", null).toLong()
    }

    fun reset() {
        databaseHelper.reset(db)
    }

    inner class PhoneCursor(private val c: Cursor) : CursorWrapper(c) {
        val title: String
            get() {
                val idx = c.getColumnIndex(KEY_TITLE)
                return c.getString(idx)
            }

        val content: String
            get() {
                val idx = c.getColumnIndex(KEY_CONTENT)
                return c.getString(idx)
            }

        val createTime: String
            get() {
                val idx = c.getColumnIndex(KEY_CREATE_TIME)
                return c.getString(idx)
            }

        val modifyTime: String
            get() {
                val idx = c.getColumnIndex(KEY_MODIFY_TIME)
                return c.getString(idx)
            }

        val imagePath: String
            get() {
                val idx = c.getColumnIndex(KEY_IMAGE_PATH)
                return c.getString(idx)
            }
    }

    internal inner class DatabaseHelper : SQLiteOpenHelper(context, DB_NAME, null, version) {
        override fun onCreate(db: SQLiteDatabase) {
            val sql = "create table $TABLE_NAME (" +
                    "_id integer primary key autoincrement, " +
                    "$KEY_TITLE text, $KEY_CONTENT text, $KEY_LOOK_UP text, " +
                    "$KEY_CREATE_TIME text, $KEY_MODIFY_TIME text, $KEY_IMAGE_PATH text)"
            db.execSQL(sql)
            val cv = encodeContentValues("周末要完成的作业", "Android 的小实验和大实验", "")
            val s = cv[KEY_MODIFY_TIME] as String
            cv.put(KEY_CREATE_TIME, s)
            db.insert(TABLE_NAME, null, cv)
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

    private fun encodeContentValues(title: String, content: String, imagePath: String): ContentValues {
        val cv = ContentValues()
        cv.put(KEY_TITLE, title)
        cv.put(KEY_CONTENT, content)
        cv.put(KEY_LOOK_UP, generateLookup(title))
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val s = simpleDateFormat.format(date)
        cv.put(KEY_MODIFY_TIME, s)
        cv.put(KEY_IMAGE_PATH, imagePath)
        return cv
    }

    private fun generateLookup(name: String): String {
        return PinyinUtils.generateLookup(name)
    }


    companion object {
        private const val DB_NAME = "phone.db"
        const val TABLE_NAME: String = "contact"
        const val KEY_TITLE: String = "title"
        const val KEY_CONTENT: String = "content"
        const val KEY_LOOK_UP: String = "look_up"
        const val KEY_MODIFY_TIME: String = "modify_time"
        const val KEY_CREATE_TIME: String = "create_time"
        const val KEY_IMAGE_PATH: String = "image_path"
    }
}

