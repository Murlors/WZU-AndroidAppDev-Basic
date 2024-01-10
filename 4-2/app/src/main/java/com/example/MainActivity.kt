package com.example.hlt

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var listViewAdapter: ArrayAdapter<String>
    private var list = (0..9).map { "Item%02d".format(it) }.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.list_view)
        listViewAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = listViewAdapter
        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.opt_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.opt_new -> {
                list.add(info.position, "Random%03d".format((0..999).random()))
                listViewAdapter.notifyDataSetChanged()
            }
            R.id.opt_delete -> {
                list.removeAt(info.position)
                listViewAdapter.notifyDataSetChanged()
            }
        }
        return super.onContextItemSelected(item)
    }
}