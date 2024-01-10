package com.example.hlt

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.hlt.PhoneDialog.OnSubmitListener
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

        registerForContextMenu(lv)
        lv.onItemClickListener = OnItemClickListener { _, _, _, id ->
            val cur: PhoneDatabase.PhoneCursor = db.queryById(id)
            cur.moveToFirst()
            val phone: String = cur.phone
            cur.close()
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone"))
            startActivity(intent)
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.opt_insert -> {
                insertRandomData()
                updateListView()
            }

            R.id.opt_reset -> {
                db.reset()
                updateListView()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.ctx_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val menuInfo = item.menuInfo as AdapterContextMenuInfo
        val id = menuInfo.id
        when (item.itemId) {
            R.id.ctx_add -> addData()
            R.id.ctx_edit -> modifyData(id)
            R.id.ctx_delete -> {
                db.deleteData(id)
                updateListView()
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun modifyData(id: Long) {
        val c = db.queryById(id)
        c.moveToFirst()
        val name = c.name
        val phone = c.phone
        c.close()
        val dialog = PhoneDialog(this, "Edit for _id=$id")
        dialog.showDialog(name, phone, object : OnSubmitListener {
            override fun onSubmit(updatedName: String, updatedPhone: String) {
                db.updateData(updatedName, updatedPhone, id)
                updateListView()
            }
        })
    }

    private fun addData() {
        val dialog = PhoneDialog(this, "Create new data")
        dialog.showDialog("", "", object : OnSubmitListener {
            override fun onSubmit(updatedName: String, updatedPhone: String) {
                db.insertData(updatedName, updatedPhone)
                updateListView()
            }
        })
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
}