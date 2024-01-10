package com.example.hlt

import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var db: PhoneDatabase = PhoneDatabase(this)
    private lateinit var adapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lv = findViewById<ListView>(R.id.listView)
        db.open()
        val c: Cursor = db.queryAll()
        val keys = arrayOf(PhoneDatabase.KEY_NAME, PhoneDatabase.KEY_PHONE, PhoneDatabase.KEY_LOOK_UP)
        val into = intArrayOf(R.id.row_view_tv_name, R.id.row_view_tv_phone, R.id.row_view_tv_lookup)
        adapter = SimpleCursorAdapter(this, R.layout.row_view, c, keys, into)
        lv.adapter = adapter

        findViewById<Button>(R.id.bt_insert).setOnClickListener {
            insertRandomData()
            updateListView()
        }
        findViewById<Button>(R.id.bt_reset).setOnClickListener {
            db.reset()
            updateListView()
        }
    }

    private fun updateListView() {
        adapter.cursor.requery()
    }

    private fun insertRandomData() {
        val s1 = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨"
        val s2 = "甲乙丙丁戊己庚辛一二三四五六七八"
        val n1 = Random.nextInt(s1.length)
        val n2 = Random.nextInt(s2.length)
        val name = "${s1[n1]}${s2[n2]}"
        val phone = "%04d".format(Random.nextInt(10000))
        db.insertData(name, phone)
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.opt_menu, menu)
        val item = menu.findItem(R.id.opt_search)
        val searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val c: Cursor = db.fuzzyQuery(newText)
                adapter.swapCursor(c)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}