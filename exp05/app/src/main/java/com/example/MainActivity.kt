package com.example.hlt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    val db: PhoneDatabase by lazy { PhoneDatabase(applicationContext) }
    lateinit var adapter: SimpleCursorAdapter
    private lateinit var launcher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtils.requestPermissions(this, "android.permission.READ_EXTERNAL_STORAGE")

        db.open()
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                updateListview()
            }
        }
        val cursor = db.queryAll()
        val lv = findViewById<ListView>(R.id.listView)
        val from = arrayOf(PhoneDatabase.KEY_TITLE, PhoneDatabase.KEY_CREATE_TIME)
        val to = intArrayOf(R.id.row_view_tv_title, R.id.row_view_tv_time)
        adapter = SimpleCursorAdapter(this, R.layout.row_view, cursor, from, to)
        lv.adapter = adapter
        lv.onItemClickListener = OnItemClickListener { _, _, _, id ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            val b = Bundle()
            b.putLong(KEY_ID, id)
            b.putBoolean(KEY_IS_NEW_DATA, false)
            intent.putExtras(b)
            launcher.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    private fun myReset() {
        db.reset()
        updateListview()
    }

    private fun updateListview() {
        adapter.cursor.requery()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.opt_menu, menu)

        val item = menu.findItem(R.id.opt_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val cursor = db.fuzzyQuery(newText)
                adapter.swapCursor(cursor)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.opt_new -> {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                val b = Bundle()
                b.putBoolean(KEY_IS_NEW_DATA, true)
                intent.putExtras(b)
                launcher.launch(intent)
            }

            R.id.opt_reset -> myReset()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val KEY_ID: String = "key_id"
        const val KEY_IS_NEW_DATA: String = "key_is_new"
        fun getIntentIdData(intent: Intent): Long {
            val b = intent.extras
            val id = b!!.getLong(KEY_ID)
            return id
        }

        fun getIntentIsNewData(intent: Intent): Boolean {
            val b = intent.extras
            val is_new = b!!.getBoolean(KEY_IS_NEW_DATA)
            return is_new
        }
    }
}

